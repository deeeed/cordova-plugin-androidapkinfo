/**
 */
package net.siteed;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Log;

import org.apache.commons.codec.binary.Hex;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import org.apache.commons.codec.digest.DigestUtils;


public class AndroidAPKInfoPlugin extends CordovaPlugin {
    private static final String TAG = "AndroidAPKInfoPlugin";

    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);

        Log.d(TAG, "Initializing AndroidAPKInfoPlugin");
    }

    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {

        if (action.equals("signatures")) {
            Context context = cordova.getActivity().getApplicationContext();
            String packageName = context.getPackageName();
            this.getPackageSignatures(packageName, callbackContext);
        } else if (action.equals("APKSignatures")) {
            String apkPath = args.getString(0);
            this.getAPKSignatures(apkPath, callbackContext);
        } else if (action.equals("packageSignatures")) {
            String packageName = args.getString(0);
            this.getPackageSignatures(packageName, callbackContext);
        } else {
            JSONObject errorObj = new JSONObject();
            errorObj.put("status", PluginResult.Status.INVALID_ACTION.ordinal());
            errorObj.put("message", "Invalid action");
            callbackContext.error(errorObj);
        }

        return true;
    }

    /**
     * Get the list of signatures for the APK given in parameters
     *
     * @param apkFile
     * @param callbackContext
     * @throws JSONException
     */
    private void getAPKSignatures(String apkFile, final CallbackContext callbackContext) throws JSONException {
        JSONObject resultObj = new JSONObject();

        Log.d(TAG, "getAPKSignatures for "+apkFile);

        File file = new File(apkFile);
        if (file.exists()) {
            PackageManager pm = cordova.getActivity().getPackageManager();
            Context context = cordova.getActivity().getApplicationContext();

            Signature[] sigs = pm.getPackageArchiveInfo(apkFile, PackageManager.GET_SIGNATURES).signatures;
            resultObj.put("signatures", getJSONFromSignatures(sigs));

        } else {
            JSONObject errorObj = new JSONObject();
            errorObj.put("status", PluginResult.Status.ERROR.ordinal());
            errorObj.put("message", "File not found");
            callbackContext.error(errorObj);
        }
    }

    /**
     * Get the list of signatures for the given package name.
     *
     * @param packageName
     * @param callbackContext
     * @throws JSONException
     */
    private void getPackageSignatures(String packageName, final CallbackContext callbackContext) throws JSONException {
        JSONObject resultObj = new JSONObject();

        Log.d(TAG, "getPackageSignatures for "+packageName);

        PackageManager pm = cordova.getActivity().getPackageManager();

        try {
            Signature[] sigs = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES).signatures;
            resultObj.put("signatures", getJSONFromSignatures(sigs));

            callbackContext.success(resultObj);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            callbackContext.error(resultObj);
        }
    }

    private JSONArray getJSONFromSignatures(Signature[] sigs) throws JSONException {
        JSONArray jsonSigs = new JSONArray();
        for (Signature sig : sigs) {
            Log.i(TAG, "Signature hashcode : " + sig.hashCode());
            JSONObject jsonSig = new JSONObject();

            // Cannot directly use DigestUtils hex methods
            // See https://stackoverflow.com/questions/9126567/method-not-found-using-digestutils-in-android
            try {
                String md5 = new String(Hex.encodeHex(DigestUtils.md5(sig.toByteArray())));
                jsonSig.put("md5", md5);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                String sha1 = new String(Hex.encodeHex(DigestUtils.sha1(sig.toByteArray())));
                jsonSig.put("sha1", sha1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                String sha256 = new String(Hex.encodeHex(DigestUtils.sha256(sig.toByteArray())));
                jsonSig.put("sha256", sha256);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonSig.put("hashcode", sig.hashCode());
            jsonSig.put("charsString", sig.toCharsString());
            jsonSig.put("info", getSignatureInfo(sig));
            jsonSigs.put(jsonSig);
        }
        return jsonSigs;
    }

    /**
     * Get x509 cert info about a signature
     *
     * @param sig
     * @return JSONObject
     * @throws JSONException
     */
    private JSONObject getSignatureInfo(Signature sig) throws JSONException {
        JSONObject resultObj = new JSONObject();
        try {
            final byte[] rawCert = sig.toByteArray();
            InputStream certStream = new ByteArrayInputStream(rawCert);

            CertificateFactory certFactory = CertificateFactory.getInstance("X509");
            X509Certificate x509Cert = (X509Certificate) certFactory.generateCertificate(certStream);


            resultObj.put("subject_dn", x509Cert.getSubjectDN());
            resultObj.put("issuer_dn", x509Cert.getIssuerDN());
            resultObj.put("serialnumber", x509Cert.getSerialNumber());

        } catch (CertificateException e) {
            resultObj.put("error", "invalid certificate");
        }
        return resultObj;
    }
}

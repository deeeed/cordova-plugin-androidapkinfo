# cordova-plugin-androidapkinfo
A cordova plugin to retrieve android package signatures info

## Installation
```
cordova plugin add cordova-plugin-androidapkinfo
```

## How to use

 ```
 //once device is ready
AndroidAPKInfoPlugin.signatures(function(result) {
  console.debug("result", result);
})

AndroidAPKInfoPlugin.APKSignatures("file:///path/to/package.apk", function(result) {
  console.debug("result", result);
})

AndroidAPKInfoPlugin.packageSignatures("com.company.packagename", function(result) {
  console.debug("result", result);
})
 ```

### Description of the result object
```json
{
  "signatures": [
      {
        "charString": "aeawqwq....",
        "hashcode": -1742539072,
        "md5": "93696578d4732e2a26fe716c87fa4a27",
        "sha1": "d5b10f4aa415448994424e3e2b6d131d9caa03ad",
        "sha256": "58241fba136c1e2c82a55b54b456ac2ed4be2e0f0c1a78cd08c0833e67a9e6ee",
        "info": {
          "issuer_dn": "CN=Android Debug, O=Android, C=US",
          "serialnumber": 1194529253,
          "subject_dn": "CN=Android Debug, O=Android, C=US"
        }
      }
  ]
}
```

## Contributing

1. Fork it
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create new Pull Request

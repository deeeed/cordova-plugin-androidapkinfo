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
        "hashcode": 342234324234,
        "info": {
          "issuer_dn": "CN=Android Debug, O=Android, C=US",
          "serialnumber": 13242134123,
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

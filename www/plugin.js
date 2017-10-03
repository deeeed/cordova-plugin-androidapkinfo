
var exec = require('cordova/exec');

var PLUGIN_NAME = 'AndroidAPKInfoPlugin';

var AndroidAPKInfoPlugin = {
  signatures: function(cb) {
    exec(cb, null, PLUGIN_NAME, 'signatures', []);
  },
  APKSignatures: function(params, cb) {
    exec(cb, null, PLUGIN_NAME, 'APKSignatures', [params]);
  },
  packageSignatures: function(params, cb) {
    exec(cb, null, PLUGIN_NAME, 'packageSignatures', [params]);
  }
};

module.exports = AndroidAPKInfoPlugin;

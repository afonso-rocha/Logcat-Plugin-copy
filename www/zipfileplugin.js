var exec = require('cordova/exec');

exports.sendLogs = function (success, error) {
    exec(success, error, 'ZipFilesPlugin', 'uploadPlugin', []);
};
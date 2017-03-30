//////////////////////////////////////////
// Cache.js
// Copyright (C) 2014 Modern Alchemits OG <office@modalog.at>
//
//////////////////////////////////////////
var exec = require('cordova/exec');

var Cache =
{
    clear : function( success, error )
    {
        exec(success, error, "Cache", "clear", [])
    },
    clearAllData : function( success, error )
    {
        exec(success, error, "Cache", "clearAllData", [])
    },
    savePreference:function(success, error, values)
    {
     exec(success, error, "Cache", "savePreference", [values])
    },
    getPreference:function(success, error, value)
    {
        exec(success, error, "Cache", "getPreference", [value])
    }
}

module.exports = Cache;

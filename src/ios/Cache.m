/*
 Copyright 2014 Modern Alchemists OG

 Licensed under MIT.

 Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 to permit persons to whom the Software is furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.
*/


#import "Cache.h"

@implementation Cache

@synthesize command;

- (void)pluginInitialize
{
        NSLog(@"INIT PLUGIN.");
}


- (void)clear:(CDVInvokedUrlCommand*)command
{
    NSLog(@"Cordova iOS Cache.clear() called.");

    self.command = command;

    // Arguments arenot used at the moment.
    // NSArray* arguments = command.arguments;


    // clear cache
    [[NSURLCache sharedURLCache] removeAllCachedResponses];
    [[NSURLCache sharedURLCache] setDiskCapacity:0];
    [[NSURLCache sharedURLCache] setMemoryCapacity:0];

    [self success];
}

- (void)clearAllData:(CDVInvokedUrlCommand*)command
{
    NSLog(@"Cordova iOS Cache.clear() called.");

    self.command = command;

    // Arguments arenot used at the moment.
    // NSArray* arguments = command.arguments;

    //[self.commandDelegate runInBackground:^{

        // clear cache
        [[NSURLCache sharedURLCache] removeAllCachedResponses];
        [[NSURLCache sharedURLCache] setDiskCapacity:0];
        [[NSURLCache sharedURLCache] setMemoryCapacity:0];

    //}];

        // NSSet *websiteDataTypes = [NSSet setWithArray:@[
        //                 WKWebsiteDataTypeDiskCache,
        //                 WKWebsiteDataTypeOfflineWebApplicationCache,
        //                 WKWebsiteDataTypeMemoryCache,
        //                 WKWebsiteDataTypeLocalStorage,
        //                 WKWebsiteDataTypeCookies,
        //                 WKWebsiteDataTypeSessionStorage,
        //                 WKWebsiteDataTypeIndexedDBDatabases,
        //                 WKWebsiteDataTypeWebSQLDatabases
        //                 ]];
        //// All kinds of data
        NSSet *websiteDataTypes = [WKWebsiteDataStore allWebsiteDataTypes];
        //// Date from
        NSDate *dateFrom = [NSDate dateWithTimeIntervalSince1970:0];
        //// Execute
        [[WKWebsiteDataStore defaultDataStore] removeDataOfTypes:websiteDataTypes modifiedSince:dateFrom completionHandler:^{
            // Done
            NSLog(@"remove done");
            [self success];
        }];

    //[self success];
}

- (void)success
{
    NSString* resultMsg = @"Cordova iOS webview cache cleared.";
    NSLog(@"%@",resultMsg);

    // create acordova result
    CDVPluginResult* result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK
                                                messageAsString:[resultMsg stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];

    // send cordova result
    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
}

- (void)error:(NSString*)message
{
    NSString* resultMsg = [NSString stringWithFormat:@"Error while clearing webview cache (%@).", message];
    NSLog(@"%@",resultMsg);

    // create cordova result
    CDVPluginResult* result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                                messageAsString:[resultMsg stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];

    // send cordova result
    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];

}

@end

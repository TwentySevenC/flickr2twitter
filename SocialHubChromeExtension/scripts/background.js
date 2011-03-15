function greet(){
    console.log("[Func] greet");
    
    chrome.tabs.create({
        'url': 'http://ebaysocialhub.appspot.com/'
    });
}

chrome.extension.onRequest.addListener(function(request, sender, sendResponse){
    console.log(sender.tab ? "from a content script: " + sender.tab.url : "from the extension");
    
    if (request.SELLER_ID) {
        handleRequest(request.SELLER_ID, sendResponse);
    }
    else {
        sendResponse({
            MESSAGE: "Error: Empty seller ID."
        });
    }
});

function handleRequest(sellId, sendResponse){
    console.log("[Func] handleRequest");
    
    // check user ID
    checkUserId();
    
    // TODO#EMAC.P1 send request to SocialHub
    var succeeded = true;
    
    // return to user
    if (succeeded) {
        sendResponse({
            MESSAGE: "Followed!"
        });
    }
}

var userId = "";

function checkUserId(){
    console.log("[Func] checkUserId");
    
    // already bound
    if (userId) {
        return;
    }
    
    // search cookies
    console.log("Search user ID in cookies ..");
    chrome.cookies.get({
        url: "http://ebaysocialhub.appspot.com/chromeextension/",
        name: "userId"
    }, function(cookie){
        if (cookie) {
            userId = cookie.value;
            console.log("Found user ID in cookies: " + userId);
        }
    });
    
    if (!userId) {
        // request via Oauth
        console.log("Request user ID via Oauth ..");
        requestUserId();
    }
}

var oauth = ChromeExOAuth.initBackgroundPage({
    'request_url': 'https://www.google.com/accounts/OAuthGetRequestToken',
    'authorize_url': 'https://www.google.com/accounts/OAuthAuthorizeToken',
    'access_url': 'https://www.google.com/accounts/OAuthGetAccessToken',
    'consumer_key': 'anonymous',
    'consumer_secret': 'anonymous',
    'scope': 'http://www.google.com/m8/feeds/',
    'app_name': 'SocialHub - eBay (Chrome Extension)'
});

function requestUserId(){
    console.log("[Func] requestUserId");
    
    oauth.authorize(onAuthorized);
}

function onAuthorized(){
    console.log("[Func] onAuthorized");
    
    var url = "http://www.google.com/m8/feeds/contacts/default/full";
    oauth.sendSignedRequest(url, onRequestUserId, {
        'parameters': {
            'alt': 'json',
            'max-results': 0
        }
    });
}

function onRequestUserId(text, xhr){
    console.log("[Func] onRequestUserId");
    
    var data = JSON.parse(text);
    userId = data.feed.author[0].email.$t;
    console.log("Got user ID: " + userId);
    
    console.log("Save user ID in cookies ..");
    chrome.cookies.set({
        url: "http://ebaysocialhub.appspot.com/chromeextension/",
        name: "userId",
        value: userId,
        expirationDate: (new Date().getTime() + 86400)
    });
}

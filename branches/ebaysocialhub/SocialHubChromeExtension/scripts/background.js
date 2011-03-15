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

var userId = "";

handleRequest.sellerId = "";
handleRequest.sendResponseCallback = null;

function handleRequest(sellerId, sendResponse){
    console.log("[Func] handleRequest");
    
    // cache sellerId and sendResponse
    handleRequest.sellerId = sellerId;
    handleRequest.sendResponseCallback = sendResponse;
    
    // already bound
    if (userId) {
        submitRequest();
        
        return;
    }
    
    // request via Oauth
    console.log("Request user ID via Oauth ..");
    requestUserIdAndSubmit();
}

function submitRequest(){
    console.log("[Func] submitRequest");
    
    if (!userId) {
        return;
    }
    
    doSubmit();
}

doSubmit.xhr = null;

function doSubmit(){
    console.log("[Func] doSubmit");
    
    var method = "POST";
	var url = "http://ebaysocialhub.appspot.com/rest/eBaySeller";
    var data = userId + "/" + handleRequest.sellerId;
    
    doSubmit.xhr = new XMLHttpRequest();
    doSubmit.xhr.onreadystatechange = onDoSubmit;
    doSubmit.xhr.open(method, url, true);
    doSubmit.xhr.send(data);
}

function onDoSubmit(){
    console.log("[Func] onDoSubmit");
    
    console.log(doSubmit.xhr);
    
    var message = "";
    if (doSubmit.xhr.readyState == 4) {
        if (doSubmit.xhr.status == 200) {
            message = "Followed!";
        }
        else {
            message = "Error: Problem retrieving data."
        }
        
        handleRequest.sendResponseCallback({
            MESSAGE: message
        });
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

function requestUserIdAndSubmit(){
    console.log("[Func] requestUserIdAndSubmit");
    
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
    
    // submit request right after we got user ID
    submitRequest();
}

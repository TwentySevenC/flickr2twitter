function authorizeSellerButton(){
    console.log("[Func] authorizeSellerButton");
    
    var elms = document.getElementsByClassName("mbg");
    for (i in elms) {
        try {
            if (elms[i].nodeName == "DIV") {
                console.log("<div> found: " + elms[i]);
                
                var span = document.createElement("span");
                span.className = "bn-b psb-b psb-S";
                
                var input = document.createElement("input");
                input.value = "Follow";
                input.type = "button";
                input.addEventListener("click", doFollow);
                
                span.appendChild(input);
                elms[i].appendChild(span);
                break;
            }
        } 
        catch (err) {
            console.error(err);
        }
    }
}

function doFollow(){
    console.log("[Func] doFollow");
    
    var sellerId = getSellerId();
    console.log("Seller ID: " + sellerId);
    
    chrome.extension.sendRequest({
        SELLER_ID: sellerId
    }, function(response){
        handleResponse(response);
    });
}

function handleResponse(response){
    console.log("[Func] handleResponse");
    
    if (response.MESSAGE) {
        alert(response.MESSAGE);
    }
}

function getSellerId(){
    console.log("[Func] getSellerId");
    
    var elms = document.getElementsByClassName("mbg-nw");
    for (i in elms) {
        try {
            if (elms[i].nodeName == "SPAN") {
                console.log("<span> found: " + elms[i]);
                
                return elms[i].innerHTML;
            }
        } 
        catch (err) {
            console.error(err);
        }
    }
}

authorizeSellerButton();

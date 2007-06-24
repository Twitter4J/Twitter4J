function getAbsoluteTop(el) {
    var top = 0;
    while (el) {
        top += el.offsetTop;
        el = el.offsetParent;
    }
    return top;
}
var targetY;
var timer;
var targetID;
var lastY;
function scrollGracefully(id) {
    if (-1 != id.indexOf("#")) {
        id = id.split("#")[1];
    }
    clearTimeout(timer);
    targetY = -1;
    var anchors = document.anchors;
    for (var i = 0; i < anchors.length; i++) {
        if (anchors[i].name == id) {
            targetY = getAbsoluteTop(anchors[i]);
            break;
        }
    }
    targetID = id;
    if (targetY == -1) {
        // the anchor was not found
        return true;
    } else {
        timer = setTimeout(periodicalScroll, 50);
        return false;
    }
}
function periodicalScroll() {
    var currentY = window.scrollTop || document.documentElement.scrollTop || document.body.scrollTop;
    var delta = targetY - currentY;
    if (currentY == lastY) {
        clearTimeout(timer);
        location.hash = targetID;
    } else {
        lastY = currentY;
        scrollBy(0, delta / 3);
        if ((delta > 0 ? delta : -delta) > 3) {
            timer = setTimeout(periodicalScroll, 50);
        } else {
            clearTimeout(timer);
            location.hash = targetID;
            //    scrollTo(0,targetY);
        }
    }
}

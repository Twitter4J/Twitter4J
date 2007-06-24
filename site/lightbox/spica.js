// spica javascript libraries - spica.js -- version 0.05
// == written by Takuya Otani <takuya.otani@gmail.com> ===
// == Copyright (C) 2006 SimpleBoxes/SerendipityNZ Ltd. ==

// === array ===
if (!Array.prototype.pop)
{
    Array.prototype.pop = function()
    {
        if (!this.length) return null;
        var last = this[this.length - 1];
        --this.length;
        return last;
    }
}
if (!Array.prototype.push)
{
    Array.prototype.push = function()
    {
        for (var i = 0,n = arguments.length; i < n; i++)
            this[this.length] = arguments[i];
        return this.length;
    }
}
if (!Array.prototype.indexOf)
{
    Array.prototype.indexOf = function(value, idx)
    {
        if (typeof(idx) != 'number') idx = 0;
        else if (idx < 0) idx = this.length + idx;
        for (var i = idx,n = this.length; i < n; i++)
            if (this[i] === value) return i;
        return -1;
    }
}
// === browser ===
function Browser()
{
    this.name = navigator.userAgent;
    this.isWinIE = this.isMacIE = false;
    this.isGecko = this.name.match(/Gecko\//);
    this.isSafari = this.name.match(/AppleWebKit/);
    this.isKHTML = this.isSafari || navigator.appVersion.match(/Konqueror|KHTML/);
    this.isOpera = window.opera;
    this.hasNS = (document.documentElement) ? document.documentElement.namespaceURI : null;
    if (document.all && !this.isGecko && !this.isSafari && !this.isOpera)
    {
        this.isWinIE = this.name.match(/Win/);
        this.isMacIE = this.name.match(/Mac/);
    }
}
var Browser = new Browser();
// === event ===
if (!window.Event) var Event = new Object;
Event = {
    cache : false,
    getEvent : function(evnt)
    {
        return (evnt) ? evnt : ((window.event) ? window.event : null);
    },
    getKey : function(evnt)
    {
        evnt = this.getEvent(evnt);
        return (evnt.which) ? evnt.which : evnt.keyCode;
    },
    stop : function(evnt)
    {
        try {
            evnt.stopPropagation()
        } catch(err) {
        }
        ;
        evnt.cancelBubble = true;
        try {
            evnt.preventDefault()
        } catch(err) {
        }
        ;
        return (evnt.returnValue = false);
    },
    register : function(object, type, handler)
    {
        if (type == 'keypress' && (Browser.isKHTML || object.attachEvent)) type = 'keydown';
        if (type == 'mousewheel' && Browser.isGecko) type = 'DOMMouseScroll';
        if (!this.cache) this.cache = [];
        if (object.addEventListener)
        {
            this.cache.push([object,type,handler]);
            object.addEventListener(type, handler, false);
        }
        else if (object.attachEvent)
        {
            this.cache.push([object,type,handler]);
            object.attachEvent(['on',type].join(''), handler);
        }
        else
        {
            object[['on',type].join('')] = handler;
        }
    },
    deregister : function(object, type, handler)
    {
        if (type == 'keypress' && (Browser.isKHTML || object.attachEvent)) type = 'keydown';
        if (type == 'mousewheel' && Browser.isGecko) type = 'DOMMouseScroll';
        if (object.removeEventListener)
            object.removeEventListener(type, handler, false);
        else if (object.detachEvent)
            object.detachEvent(['on',type].join(''), handler);
        else
            object[['on',type].join('')] = null;
    },
    deregisterAll : function()
    {
        if (!Event.cache) return
        for (var i = 0,n = Event.cache.length; i < n; i++)
        {
            Event.deregister(Event.cache[i]);
            Event.cache[i][0] = null;
        }
        Event.cache = false;
    }
};
Event.register(window, 'unload', Event.deregisterAll);
// === dom ===
document.getElemetsByClassName = function(name, target)
{
    var result = [];
    var object = null;
    var search = new RegExp(['(^|\\s)',name,'(\\s|$)'].join(''));
    if (target && target.getElementsByTagName)
        object = target.getElementsByTagName('*');
    if (!object)
        object = document.getElementsByTagName ? document.getElementsByTagName('*') : document.all;
    for (var i = 0,n = object.length; i < n; i++)
    {
        var check = object[i].getAttribute('class') || object[i].className;
        if (check.match(search)) result.push(object[i]);
    }
    return result;
}
// === library ===
function Library()
{
    this._path = '';
    this._cache = [];
    this.lang = '';
    this.base = '';
    return this._init();
}
Library.prototype = {
    _init : function()
    {
        var rs_path = document.getElementsByName('X-Resource-Dir')[0];
        var js_path = document.getElementsByName('X-Script-Dir')[0];
        if (rs_path)
        {
            this.base = this._check_path(rs_path.getAttribute('content'));
            if (!js_path) this._path = [this.base,'js/'].join('');
        }
        if (js_path)
            this._path = this._check_path(js_path.getAttribute('content'));
        return this;
    },
    _initLang : function()
    {
        var html = document.getElementsByTagName('html')[0];
        if (!html) return;
        this.lang = html.getAttribute('xml:lang') || html.getAttribute('lang');
    },
    _check_path : function(path)
    {
        if (!path) return '';
        if (!path.match(/\/$/)) path = [path,'/'].join('');
        return path;
    },
    require : function(libs)
    {
        var pre = '\n<script type="text/javascript" src="';
        var post = '.js"></script>';
        for (var i = 0,n = libs.length; i < n; i++)
        {
            if (this._cache.indexOf(libs[i]) > -1) continue;
            document.write([pre,this._path,libs[i],post].join(''));
            this._cache.push(libs[i]);
        }
    },
    path : function(path)
    {
        this._path = this._check_path(path);
    }
}
var Library = new Library();
Event.register(window, 'load', function() {
    Library._initLang()
});

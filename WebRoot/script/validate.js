function isNum(str) {
	if(str==""){return true;}
    return isInteger(str)||isDecimal(str);
}
function isInteger(str) {
    if(str==""){return true;}
    return /^\d{1,}$/.test(str);
}
function isFraction(str) {
    if(str==""){return true;}
    return /^\d{0,}\/?\d{0,}$/.test(str);
}
function isDecimal(str) {
    if(str==""){return true;}
	return /^\d{1,}\.\d{0,}\d$/.test(str);
}
function isEmail(str) {
	if(str==""){return true;}
    return /^[^@.]{1,}@[^@.]{1,}\.[^@.]{1,}$/.test(str);
}
function isDate(str) {
    if(str==""){return true;}
    var regExp = /^(\d{2,4})-(\d{1,2})-(\d{1,2})$/;
	var arr = regExp.exec(str);
	try{var d = new Date(arr[1],arr[2],arr[3]);}catch(e){
		return false;
    }
	return true;
}
function isIP(s) {
	if(str==""){return true;}
    return /^\d{1,3}.\d{1,3}.\d{1,3}.\d{1,3}$/.test(str);
}
function absLength(str) {
	var num=0;
	for(var i=0,ii=str.length;i<ii;i++){
		if(str.charCodeAt(i)<=255){
			num++;
		}else{
			num+=2;
		}
	}
	return num;
}
function isIncludeHtmlChar(inputString) {
    var allHTMLChar = "&<>\"'";
    for (i = 0; i < inputString.length; i++) {
        if (allHTMLChar.indexOf(inputString.charAt(i)) >= 0) {
            return true;
        }
    }
    return false;
}
function radioChecked(elemt) {
    var b = false;
    if (elemt.length) {
        for (i = 0; i < elemt.length; i++) {
            if (elemt[i].checked) {
                b = true;
            }
        }
    } else {
        if (elemt.checked) {
            b = true;
        }
    }
    return b;
}
function isWildChar(inputString) {
    var allWildChar = "%?";
    for (i = 0; i < inputString.length; i++) {
        if (allWildChar.indexOf(inputString.charAt(i)) >= 0) {
            return true;
        }
    }
    return false;
}
function isNamingChar(inputString) {
    for (i = 0; i < inputString.length; i++) {
        if (inputString.charCodeAt(i) > 128) {
            return false;
        }
        if (inputString.charAt(i) >= "0" && inputString.charAt(i) <= "9") {
            continue;
        }
        if (inputString.charAt(i) >= "A" && inputString.charAt(i) <= "Z") {
            continue;
        }
        if (inputString.charAt(i) >= "a" && inputString.charAt(i) <= "z") {
            continue;
        }
        if (inputString.charAt(i) == "-") {
            continue;
        }
        if (inputString.charAt(i) == "_") {
            continue;
        }
        if (inputString.charAt(i) == ".") {
            continue;
        }
        return false;
    }
    return true;
}
function isNumber(inputString) {
    var allNumber = "0123456789.";
    for (i = 0; i < inputString.length; i++) {
        var j = allNumber.indexOf(inputString.charAt(i));
        if (j < 0) {
            return false;
        }
    }
    return true;
}
function isTelNum(inputString) {
    var allNumber = "-0123456789.";
    for (i = 0; i < inputString.length; i++) {
        var j = allNumber.indexOf(inputString.charAt(i));
        if (j < 0) {
            return false;
        }
    }
    return true;
}
function isBetween(val, lo, hi) {
    if ((val < lo) || (val > hi)) {
        return false;
    } else {
        return true;
    }
}
function isPostalcode(inputString) {
	if(isInteger(inputString)&&inputString.length == 6){
        return true;
	}
    return false;
}
function isMobilePhone(inputString) {
	if(isInteger(inputString)&&inputString.length == 11){
        return true;
	}
    return false;
}

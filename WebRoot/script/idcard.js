function IDcard(){
	var vcity = {
	    11 : "北京",
	    12 : "天津",
	    13 : "河北",
	    14 : "山西",
	    15 : "内蒙古",
	    21 : "辽宁",
	    22 : "吉林",
	    23 : "黑龙江",
	    31 : "上海",
	    32 : "江苏",
	    33 : "浙江",
	    34 : "安徽",
	    35 : "福建",
	    36 : "江西",
	    37 : "山东",
	    41 : "河南",
	    42 : "湖北",
	    43 : "湖南",
	    44 : "广东",
	    45 : "广西",
	    46 : "海南",
	    50 : "重庆",
	    51 : "四川",
	    52 : "贵州",
	    53 : "云南",
	    54 : "西藏",
	    61 : "陕西",
	    62 : "甘肃",
	    63 : "青海",
	    64 : "宁夏",
	    65 : "新疆",
	    71 : "台湾",
	    81 : "香港",
	    82 : "澳门",
	    91 : "国外"
	};
	
	this.check = function(card) {
	    //是否为空
	    if (card == '') {
	        alert('请输入身份证号，身份证号不能为空');
	        return false;
	    }
	    if (card.length != 18) {
	        alert('您输入的身份证号码不正确，请重新输入18位身份证号码');
	        return false;
	    }
	    //校验长度，类型
	    if (this.isCardNo(card) === false) {
	        alert('您输入的身份证号码不正确，请重新输入');
	        return false;
	    }
	    //检查省份
	    if (this.checkProvince(card) === false) {
	        alert('您输入的身份证号码不正确,请重新输入');
	        return false;
	    }
	    //校验生日
	    if (this.checkBirthday(card) === false) {
	        alert('您输入的身份证号码生日不正确,请重新输入');
	        return false;
	    }
	    /*
	    //检验位的检测
	    if (this.checkParity(card) === false) {
	        alert('您的身份证校验位不正确,请重新输入');
	        return false;
	    }
	    //校验性别
	    if (this.checkSex(card) === false) {
	        alert('您的身份证校验位不正确,请重新输入');
	        return false;
	    }
	    */
	    return true;
	};
	
	//检查号码是否符合规范，包括长度，类型
	this.isCardNo = function(card) {
	    //身份证号码为15位或者18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X
	    var reg = /(^\d{17}(\d|X)$)/;
	    if (reg.test(card) === false) {
	        return false;
	    }
	    return true;
	};
	
	//取身份证前两位,校验省份
	this.checkProvince = function(card) {
	    var province = card.substr(0, 2);
	    if (vcity[province] == undefined) {
	        return false;
	    }
	    return true;
	};
	
	//检查生日是否正确
	this.checkBirthday = function(card) {
	    var len = card.length;
	    //身份证18位时，次序为省（3位）市（3位）年（4位）月（2位）日（2位）校验位（4位），校验位末尾可能为X
	    if (len == '18') {
	        var re_eighteen = /^(\d{6})(\d{4})(\d{2})(\d{2})(\d{3})([0-9]|X)$/;
	        var arr_data = card.match(re_eighteen);
	        var year = arr_data[2];
	        var month = arr_data[3];
	        var day = arr_data[4];
	        var birthday = new Date(year + '/' + month + '/' + day);
	        return this.verifyBirthday(year, month, day, birthday);
	    }
	    return false;
	};
	
	//校验日期
	this.verifyBirthday = function(year, month, day, birthday) {
	    var now = new Date();
	    var now_year = now.getFullYear();
	    //年月日是否合理
	    if ((birthday.getFullYear() == year) & ((birthday.getMonth() + 1) == month) & (birthday.getDate() == day)) {
	        //判断年份的范围（18岁到60岁之间)
	        var time = now_year - year;
	        if (time >= 18 & time <= 60) {
	            return true;
	        }else{
				alert("年龄限制在18至60岁之间");        
	        }
	        return false;
	    }
	    return false;
	};
	//校验位的检测
	this.checkParity = function(card) {
	    //15位转18位
	    var len = card.length;
	    if (len == '18') {
	        var arrInt = new Array(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8,
	                4, 2);
	        var arrCh = new Array('1', '0', 'X', '9', '8', '7', '6', '5', '4', '3',
	                '2');
	        var cardTemp = 0, i, valnum;
	        for (i = 0; i < 17; i++) {
	            cardTemp += card.substr(i, 1) * arrInt[i];
	        }
	        valnum = arrCh[cardTemp % 11];
	        if (valnum == card.substr(17, 1)) {
	            return true;
	        }
	        return false;
	    }
	    return false;
	};
	
	//校验身份证性别
	this.checkSex = function(card){
		var sex = card.substr(16, 1);
		if(sex == "1"){
			return true;
		}else if(sex == "0"){
			return true;
		}
		return false;
	}
	
}

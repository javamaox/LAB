function IDcard(){
	var vcity = {
	    11 : "����",
	    12 : "���",
	    13 : "�ӱ�",
	    14 : "ɽ��",
	    15 : "���ɹ�",
	    21 : "����",
	    22 : "����",
	    23 : "������",
	    31 : "�Ϻ�",
	    32 : "����",
	    33 : "�㽭",
	    34 : "����",
	    35 : "����",
	    36 : "����",
	    37 : "ɽ��",
	    41 : "����",
	    42 : "����",
	    43 : "����",
	    44 : "�㶫",
	    45 : "����",
	    46 : "����",
	    50 : "����",
	    51 : "�Ĵ�",
	    52 : "����",
	    53 : "����",
	    54 : "����",
	    61 : "����",
	    62 : "����",
	    63 : "�ຣ",
	    64 : "����",
	    65 : "�½�",
	    71 : "̨��",
	    81 : "���",
	    82 : "����",
	    91 : "����"
	};
	
	this.check = function(card) {
	    //�Ƿ�Ϊ��
	    if (card == '') {
	        alert('���������֤�ţ����֤�Ų���Ϊ��');
	        return false;
	    }
	    if (card.length != 18) {
	        alert('����������֤���벻��ȷ������������18λ���֤����');
	        return false;
	    }
	    //У�鳤�ȣ�����
	    if (this.isCardNo(card) === false) {
	        alert('����������֤���벻��ȷ������������');
	        return false;
	    }
	    //���ʡ��
	    if (this.checkProvince(card) === false) {
	        alert('����������֤���벻��ȷ,����������');
	        return false;
	    }
	    //У������
	    if (this.checkBirthday(card) === false) {
	        alert('����������֤�������ղ���ȷ,����������');
	        return false;
	    }
	    /*
	    //����λ�ļ��
	    if (this.checkParity(card) === false) {
	        alert('�������֤У��λ����ȷ,����������');
	        return false;
	    }
	    //У���Ա�
	    if (this.checkSex(card) === false) {
	        alert('�������֤У��λ����ȷ,����������');
	        return false;
	    }
	    */
	    return true;
	};
	
	//�������Ƿ���Ϲ淶���������ȣ�����
	this.isCardNo = function(card) {
	    //���֤����Ϊ15λ����18λ��15λʱȫΪ���֣�18λǰ17λΪ���֣����һλ��У��λ������Ϊ���ֻ��ַ�X
	    var reg = /(^\d{17}(\d|X)$)/;
	    if (reg.test(card) === false) {
	        return false;
	    }
	    return true;
	};
	
	//ȡ���֤ǰ��λ,У��ʡ��
	this.checkProvince = function(card) {
	    var province = card.substr(0, 2);
	    if (vcity[province] == undefined) {
	        return false;
	    }
	    return true;
	};
	
	//��������Ƿ���ȷ
	this.checkBirthday = function(card) {
	    var len = card.length;
	    //���֤18λʱ������Ϊʡ��3λ���У�3λ���꣨4λ���£�2λ���գ�2λ��У��λ��4λ����У��λĩβ����ΪX
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
	
	//У������
	this.verifyBirthday = function(year, month, day, birthday) {
	    var now = new Date();
	    var now_year = now.getFullYear();
	    //�������Ƿ����
	    if ((birthday.getFullYear() == year) & ((birthday.getMonth() + 1) == month) & (birthday.getDate() == day)) {
	        //�ж���ݵķ�Χ��18�굽60��֮��)
	        var time = now_year - year;
	        if (time >= 18 & time <= 60) {
	            return true;
	        }else{
				alert("����������18��60��֮��");        
	        }
	        return false;
	    }
	    return false;
	};
	//У��λ�ļ��
	this.checkParity = function(card) {
	    //15λת18λ
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
	
	//У�����֤�Ա�
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

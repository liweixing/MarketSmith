package com.marketsmith.utils;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;

public class StringUtil {
    public static String EMPTY_STRING  = "";
    public static String TIME_FORMAT   = "yyyy-MM-dd HH:mm:ss"; 
    final static String  PLEASE_SELECT = "Please select...";

    /**
     * @param str
     * @return  return true if not empty and lenth > 1
     */
    public static boolean isNotBlank(String str) {
        return str != null && !str.trim().equals(EMPTY_STRING);
    }

    /**
     * @param str
     * @return return true if empty or length = 0
     */
    public static boolean isBlank(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * after taking off blank, 
     * @param str
     * @return return true if not empty and length > 1 after taking off blank
     */
    public static boolean isNotTrimBlank(String str) {
        return str != null && !str.trim().equals(EMPTY_STRING);
    }

    public static boolean empty(Object o) {
        return o == null || "".equals(o.toString().trim()) || "null".equalsIgnoreCase(o.toString().trim())
                || "undefined".equalsIgnoreCase(o.toString().trim()) || PLEASE_SELECT.equals(o.toString().trim());
    }

    public static boolean notEmpty(Object o) {
        return o != null && !"".equals(o.toString().trim()) && !"null".equalsIgnoreCase(o.toString().trim())
                && !"undefined".equalsIgnoreCase(o.toString().trim()) && !PLEASE_SELECT.equals(o.toString().trim());
    }

    /**
     * @param str
     * @return return true if empty or length = 0 after taking off blank
     */
    public static boolean isTrimBlank(String str) {
        return str == null || str.trim().equals(EMPTY_STRING);
    }

    /**
     * do with empty string
     * @param str
     * @return String
     */
    public static String doEmpty(String str) {
        return doEmpty(str, "");
    }

    /**
     * do with empty string
     * @param str
     * @param defaultValue
     * @return String
     */
    public static String doEmpty(String str, String defaultValue) {
        if (str == null || str.equalsIgnoreCase("null") || str.trim().equals("") || str.trim().equals("－Please select－")) {
            str = defaultValue;
        } else if (str.startsWith("null")) {
            str = str.substring(4, str.length());
        }
        return str.trim();
    }

    /**
     * Capitalize the first letter
     */
    @SuppressLint("DefaultLocale")
    public static String capFirstUpperCase(String str) {
        if (isBlank(str)) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * Make the first letter low case
     */
    @SuppressLint("DefaultLocale")
    public static String capFirstLowerCase(String str) {
        if (isBlank(str)) {
            return str;
        }
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    /**
     * whether is mobile number
     * 
     * @param str
     * @return
     */
    public static boolean isMobileNumber(String str) {
        Pattern p = Pattern.compile("^((\\+?86)|((\\+86)))?1\\d{10}$");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /** 
     * whether is phone number 
     * @param  str 
     */
    public static boolean isPhone(String str) {
        Pattern p1 = null, p2 = null;
        Matcher m = null;
        boolean b = false;
        p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$");  // 验证带区号的
        p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$");         // 验证没有区号的
        if (str.length() > 9) {
            m = p1.matcher(str);
            b = m.matches();
        } else {
            m = p2.matcher(str);
            b = m.matches();
        }
        return b;
    }

    /**
     * use regular expression to verify 
     */
    public static boolean check(String str, String regex) {
        boolean flag = false;
        try {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(str);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * verify phone or fax number
     * @param fax
     * @return
     */
    public static boolean isPhoneOrFax(String fax) {
        String regex = "^(0\\d{2}(-)?\\d{7,8}(-\\d{1,4})?)|(0\\d{3}(-)?\\d{7,8}(-\\d{1,4})?)$";
        return check(fax, regex);
    }

    /**
     * verify QQ number, the first number can not be 0，others can be 0-9，length >= 5. Here it's length is 5-21.
     * @param QQ
     * @return
     */
    public static boolean isQQ(String QQ) {
        String regex = "^[1-9][0-9]{4,20}$";
        return check(QQ, regex);
    }

    // verify post code
    public static boolean isPostCode(String post) {
        if (post.matches("[1-9]\\d{5}(?!\\d)")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * verify id card number
     * 
     * @param str
     * @return
     */
    public static boolean isIdCardNumber(String str) {
        Pattern p = Pattern.compile("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{4}$");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
    * verify email
    * @return
    */
    public static boolean isEmail(String email) {
        Pattern emailPattern = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        Matcher matcher = emailPattern.matcher(email);
        if (matcher.find()) {
            return true;
        }
        return false;
    }

    /**
     * confirm password
     * 
     * @param str1
     * @param str2
     * @return
     */
    public static boolean isConfirmPassword(String str1, String str2) {
        return str1.equals(str2);
    }

    /**
     * hide some number of phone number, example: 185****6666
     * 
     * @param phone
     * @return
     */
    public static String hiddenPhoneNum(final String phone) {
        if (isMobileNumber(phone)) {
            char[] mobile = phone.toCharArray();
            for (int i = 3; i < 7; i++) {
                mobile[i] = '*';
            }
            return String.valueOf(mobile);
        }
        return "";
    }

    /**
     * hide several numbers of account, 
     * example: 882202010000201 --->  88220********201
     * 
     * @param phone
     * @return
     */
    public static String hiddenSevralNum(final String str) {
        char[] number = str.toCharArray();
        int len = number.length;
        for (int i = 3; i < len - 4; i++) {
            number[i] = '*';
        }
        return String.valueOf(number);
    }

    /**
     * hide several characters of email
     * example: 13849620635@126.com --->  126********@126.com
     * @param email
     * @return
     */
    public static String hiddenEmail(final String str) {
        char[] email = str.toCharArray();
        int len = email.length;
        for (int i = 3; i < len; i++) {
            if (email[i] == '@') {
                break;
            }
            email[i] = '*';
        }
        return String.valueOf(email);
    }

    /**
     * every 4 characters is split by a blank, take bank acccount number for example: 6222 2238 0301 1682 0060
     */
    public static String add4blank(String str) {
        str = str.replace(" ", "");
        int strLength = str.length() / 4;
        String temp = "";
        for (int i = 0; i < strLength; i++) {
            temp += str.substring(i * 4, (i + 1) * 4);
            temp += " ";
        }
        temp += str.substring(strLength * 4);
        return temp;
    }

    /**
     * format mobile number to 3 4 4 
     * 手机号码3 4 4格式, example: 185 6666 6666
     */
    public static String addmobileblank(String str) {
        if (str.replace(" ", "").length() != 11)
            return str;
        String temp = "";
        temp += str.subSequence(0, 3);
        temp += ' ';
        temp += str.substring(3, 7);
        temp += ' ';
        temp += str.substring(7);
        return temp;
    }

    /**
     * 日期格式转换（Str转Str） 2014-05-06 转为 20140506
     */
    public static String formatDate2(String str) {
        return str.replace("-", "");
    }

    /**
     * 日期格式转换（Str转Str） 20140506转为2014-05-06
     */
    public static String formatDate(String str) {
        if (str.replace("-", "").length() != 8)
            return str;
        String temp = "";
        temp += str.subSequence(0, 4);
        temp += '-';
        temp += str.substring(4, 6);
        temp += '-';
        temp += str.substring(6);
        return temp;
    }

    /**
     * 时间格式转换（Str转Str） 123312转为12:33
     */
    public static String formatTime(String str) {
        String temp = "";
        temp += str.subSequence(0, 2);
        temp += ':';
        temp += str.substring(2, 4);
        return temp;
    }

    /**
     * 将长日期格式的字符串转换为长整型 1970-09-01 12:00:00
     * 
     * @param date
     * @param format
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static long convert2long(String date) {
        try {
            if (isNotBlank(date)) {
                SimpleDateFormat sf = new SimpleDateFormat(TIME_FORMAT);
                return sf.parse(date).getTime();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0l;
    }

    public static boolean num(Object o) {
        int n = 0;
        try {
            n = Integer.parseInt(o.toString().trim());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (n > 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean decimal(Object o) {
        double n = 0;
        try {
            n = Double.parseDouble(o.toString().trim());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (n > 0.0) {
            return true;
        } else {
            return false;
        }
    }

    // 保留小数点后两位数字，但不进行四舍五入
    public static String sub2DecimalPlaceNoRoundOff(String strDouble) {
        String str;
        int position = strDouble.indexOf("."); // 计算小数点的位置
        if (position != -1) {
            if ((strDouble.length() - 1 - position) >= 2) {
                // 如果小数点后多于两位
                strDouble = strDouble.substring(0, position + 3);
                // } else {
                // // 小数点后不足两位补0
                // DecimalFormat df = new DecimalFormat("0.00");
                // strDouble = df.format(double1);
            }
        }
        return strDouble;
    }

    /**
     * 给JID返回用户名
     * 
     * @param Jid
     * @return
     */
    public static String getUserNameByJid(String Jid) {
        if (empty(Jid)) {
            return null;
        }
        if (!Jid.contains("@")) {
            return Jid;
        }
        return Jid.split("@")[0];
    }

    /**
     * 根据给定的时间字符串，返回月 日 时 分 秒
     * 
     * @param allDate
     *            like "yyyy-MM-dd hh:mm:ss SSS"
     * @return
     */
    public static String getMonthTomTime(String allDate) {
        return allDate.substring(5, 19);
    }

    /**
     * 根据给定的时间字符串，返回月 日 时 分 月到分钟
     * 
     * @param allDate
     *            like "yyyy-MM-dd hh:mm:ss SSS"
     * @return
     */
    public static String getMonthTime(String allDate) {
        return allDate.substring(5, 16);
    }
}

package com.stareme.phonenumber

import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import java.lang.IllegalStateException

fun PhoneNumberUtil.formatPhoneNumber(regionCode: String, callCode: String, phoneNumber: String): String {
    val formatter = getAsYouTypeFormatter(regionCode)
    var result = phoneNumber
    for (c in "+" + callCode + phoneNumber) {
        result = formatter.inputDigit(c)
    }
    return result.substring(callCode.length + 1).trim()
}

fun PhoneNumberUtil.formatPhoneNumber(callCode: String, phoneNumber: String): String {
    return formatPhoneNumber(getRegionCodeForCountryCode(callCode.toInt()), callCode, phoneNumber)
}

fun PhoneNumberUtil.isValidMobileNumber(regionCode: String, phoneNumber: String): Boolean {
    return try {
        val number = parse(phoneNumber, regionCode)
        val type = getNumberType(number)

        isValidNumber(number)
                && (type == PhoneNumberUtil.PhoneNumberType.MOBILE
                || type == PhoneNumberUtil.PhoneNumberType.FIXED_LINE_OR_MOBILE)
    } catch (e: NumberParseException) {
        false
    } catch (ex: IllegalStateException) {
        false
    }
}

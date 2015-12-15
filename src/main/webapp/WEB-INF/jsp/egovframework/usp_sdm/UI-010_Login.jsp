<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%
 /**
  * @Class Name : EgovLoginUsr.jsp
  * @Description : Login 인증 화면
  * @Modification Information
  * @
  * @  수정일         수정자                   수정내용
  * @ -------    --------    ---------------------------
  * @ 2009.03.03    박지욱          최초 생성
  *   2011.09.25    서준식          사용자 관리 패키지가 미포함 되었을때에 회원가입 오류 메시지 표시
  *   2011.10.27    서준식          사용자 입력 탭 순서 변경
  *  @author 공통서비스 개발팀 박지욱
  *  @since 2009.03.03
  *  @version 1.0
  *  @see
  *
  *  Copyright (C) 2009 by MOPAS  All right reserved.
  */
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" >
<link rel="stylesheet" href="<c:url value='/css/egovframework/com/com.css'/>" type="text/css">
<link rel="stylesheet" href="<c:url value='/css/egovframework/com/button.css'/>" type="text/css">
<link rel="stylesheet" type="text/css" href="<c:url value='/css/egovframework/com/common.css'/>" >
<script type="text/javaScript" language="javascript" src="<c:url value='/js/egovframework/com/uat/uia/EgovGpkiVariables.js'/>"></script>
<script type="text/javaScript" language="javascript" src="<c:url value='/js/egovframework/com/uat/uia/EgovGpkiInstall.js'/>"></script>
<script type="text/javaScript" language="javascript" src="<c:url value='/js/egovframework/com/uat/uia/EgovGpkiFunction.js'/>"></script>
<title>SDM Login</title>
<script type="text/javaScript" language="javascript">

function btnLogin_onclick()
{
	var form = document.loginForm;

    if (form.userId.value =="") {
        alert("아이디를 입력하세요");
    } else if (form.password.value =="") {
        alert("비밀번호를 입력하세요");
    } else {
    	form.submit();
    }
}

function fnInit()
{
    var form = document.loginForm;
    
    form.userId.focus();
}

</script>
</head>
<body onLoad="fnInit();">

<!--일반로그인 테이블 시작-->
<form name="loginForm" action ="<c:url value='/main.do'/>" method="post">

<table border="0" width="700" height="500">
  <tr>
    <td>
      <table width="100%" >
        <tr>
          <td align="right">
            <img src="<c:url value='/images/egovframework/usdm/login_banner.jpg'/>"/>&nbsp;&nbsp;&nbsp;&nbsp;
          </td>
        </tr>
        <tr>
          <td align="center">
            <table width="650" border="0" cellspacing="8" cellpadding="0">
              <tr>
                <td height="210" valign="top" style="background:url(<c:url value='/images/egovframework/com/uat/uia/login_bg02.gif' />) no-repeat;">
                    <table border="0" align="center" cellpadding="0" cellspacing="0">
                      <tr>
                        <td width="40" rowspan="3">
                        </td>
                        <td width="300" rowspan="3">
                          <font size="2" face="Nanumgothic">
                            먼저 로그인해야 합니다.<br>
                            로그인 계정이 없는 경우<br>
                            관리자에게 문의하시기 바랍니다.
                          </font>
                        </td>
                        <td height="70"></td>
                      </tr>
                      <tr>
                        <td>
                            <table border="0" cellpadding="0" cellspacing="0" style="width:250px;margin-left:20px;">
                              <tr>
                                <td>
                                    <table width="250" border="0" cellpadding="0" cellspacing="0">
                                      <tr>
                                        <td class="required_text" nowrap>
                                          <label for="id">ID&nbsp;&nbsp;</label>
                                        </td>
                                        <td>
                                          <input type="text" name="userId" id="userId" style="height: 16px; width: 100px; border: 1px solid #4D82B8; margin: 0px; padding: 0px; ime-mode: disabled;" tabindex="4" maxlength="10"/>
                                        </td>
                                      </tr>
                                      <tr>
                                        <td height="3"></td></tr>
                                      <tr>
                                        <td class="required_text" nowrap>
                                          <label for="password">Password&nbsp;&nbsp;</label>
                                        </td>
                                        <td>
                                          <input type="password" name="password" id="password" style="height: 16px; width: 100px; border: 1px solid #4D82B8; margin: 0px; padding: 0px; ime-mode: disabled;" maxlength="12" tabindex="5" onKeyDown="javascript:if (event.keyCode == 13) { btnLogin_onclick(); }"/>
                                        </td>
                                      </tr>
                                    </table>
                                </td>
                              </tr>
                              <tr>
                                <td height="10"></td>
                              </tr>
                              <tr>
                                <td>
                                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                      <tr align="center">
                                        <td>
                                          <a href="#LINK" onClick="btnLogin_onclick()" tabindex="7">
                                            <img src="<c:url value='/images/egovframework/com/login_btn.gif'/>"/>
                                          </a>
                                        </td>
                                      </tr>
                                    </table>
                                </td>
                              </tr>
                            </table>
                        </td>
                      </tr>
                      <tr>
                        <td height="60"></td>
                      </tr>
                    </table>
                </td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>

</form>
<!--일반로그인 테이블 끝-->
  
</body>
</html>



<html xmlns="http://www.w3.org/1999/html">

<#include "../common/header.ftl">

<body>
<div class="container">
    <div class="row clearfix">
        <div class="col-md-12 column">
            <form role="form" method="get" action="/order/seller/user/login">
                <div class="form-group">
                    <label>User Name:</label>
                    <input name="username" class="form-control" type="text" value="${(username)}" />
                </div>
                <div class="form-group">
                    <label>PassWord:</label>
                    <input name="password" class="form-control" type="text" value="${(password)}" />
                </div>
                <!--<a href="/order/seller/user/login?username=${username}" class="button">Log in</a>-->
                <br><button type="submit" class="btn btn-default active" >Log in</button>
            </form>
        </div>
    </div>
</div>
</body>
</html>
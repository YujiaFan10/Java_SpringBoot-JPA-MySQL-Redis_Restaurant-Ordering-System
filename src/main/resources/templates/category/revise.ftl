<html xmlns="http://www.w3.org/1999/html">

<#include "../common/header.ftl">

<body>

<div id="wrapper" class="toggled">
    <#--siderbar-->
    <#include "../common/nav.ftl">

    <#-- main content-->
    <div id="page-content-wrapper">
        <div class="container-fluid">
            <div class="row clearfix">
                <div class="col-md-12 column">
                    <form role="form" method="post" action="/order/seller/category/save">

                        <div class="form-group">
                            <label>Category Name:</label>
                            <input name="categoryName" class="form-control" type="text" value="${(productCategory.categoryName)!""}" />
                        </div>
                        <div class="form-group">
                            <label>Category Type:</label>
                            <input name="categoryType" class="form-control" type="number" value="${(productCategory.categoryType)!""}" />
                        </div>

                        <br><button type="submit" class="btn btn-default active" >Save</button>

                        <input hidden type="number" name="categoryId" value="${(productCategory.categoryId)!''}">

                    </form>
                </div>
            </div>
        </div>
    </div>

</div>
</body>
</html>
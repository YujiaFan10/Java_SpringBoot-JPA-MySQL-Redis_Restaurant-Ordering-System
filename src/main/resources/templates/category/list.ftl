<html>

<#include "../common/header.ftl">

<body>

<div id="wrapper" class="toggled">
    <#--siderbar-->
    <#include "../common/nav.ftl">

    <#-- main content-->
    <div id="page-content-wrapper">
        <div class="container-fluid">
            <div class="row clearfix">
                <#--main table-->
                <div class="col-md-12 column">
                    <table class="table table-condensed table-bordered">
                        <thead>
                        <tr>
                            <th>Category Id</th>
                            <th>Category Name</th>
                            <th>Type</th>
                            <th>Created</th>
                            <th>Updated</th>
                            <th>Operations</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list categoryList as category>
                            <tr>
                                <td>${category.categoryId}</td>
                                <td>${category.categoryName}</td>
                                <td>${category.categoryType}</td>
                                <td>${category.createTime?string("yyyy-MM-dd")}</td>
                                <td>${category.updateTime?string("yyyy-MM-dd")}</td>
                                <td><a href="/order/seller/category/revise?categoryId=${category.categoryId}">Revise Info</a></td>
                            </tr>
                        </#list>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

</div>
</body>
</html>
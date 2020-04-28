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
                            <th>Product Id</th>
                            <th>Product Name</th>
                            <th>Icon</th>
                            <th>Price</th>
                            <th>Stock</th>
                            <th>Description</th>
                            <th>Category</th>
                            <th>Created</th>
                            <th>Updated</th>
                            <th colspan="2">Operations</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list productInfoPage.content as productInfo>
                            <tr>
                                <td>${productInfo.productId}</td>
                                <td>${productInfo.productName}</td>
                                <td><img height="100" width="100" src="${productInfo.productIcon}" alt=""></td>
                                <td>${productInfo.productPrice}</td>
                                <td>${productInfo.productStock}</td>
                                <td>${productInfo.productDescription}</td>
                                <td>${productInfo.categoryType}</td>
                                <td>${productInfo.createTime?string("yyyy-MM-dd")}</td>
                                <td>${productInfo.updateTime?string("yyyy-MM-dd")}</td>
                                <td><a href="/order/seller/product/revise?productId=${productInfo.productId}">Revise Info</a></td>
                                <td>
                                    <#if productInfo.getProductStatusEnum().code==0>
                                        <a href="/order/seller/product/putOff?productId=${productInfo.productId}">Put Off</a>
                                    <#else>
                                        <a href="/order/seller/product/putOn?productId=${productInfo.productId}">Put On</a>
                                    </#if>
                                </td>
                            </tr>
                        </#list>
                        </tbody>
                    </table>
                </div>

                <#--page control-->
                <div class="col-md-12 column">
                    <ul class="pagination pull-right">
                        <#if currentPage lte 1>
                            <li class="disabled"><a href="#">Prev</a></li>
                        <#else>
                            <li><a href="/order/seller/product/list?page=${currentPage-1}&size=${size}">Prev</a></li>
                        </#if>

                        <#list 1..productInfoPage.getTotalPages() as index>
                            <#if currentPage == index>
                                <li class="disabled"><a href="#">${index}</a></li>
                            <#else>
                                <li><a href="/order/seller/product/list?page=${index}&size=${size}">${index}</a></li>
                            </#if>
                        </#list>

                        <#if currentPage gte productInfoPage.getTotalPages()>
                            <li class="disabled"><a href="#">Next</a></li>
                        <#else>
                            <li><a href="/order/seller/order/list?page=${currentPage+1}&size=${10}">Next</a></li>
                        </#if>
                    </ul>
                </div>
            </div>
        </div>
    </div>

</div>
</body>
</html>
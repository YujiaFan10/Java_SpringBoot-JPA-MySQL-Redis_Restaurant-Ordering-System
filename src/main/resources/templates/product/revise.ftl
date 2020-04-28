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
                    <form role="form" method="post" action="/order/seller/product/save">
                        <div class="form-group">
                            <label>Product Name:</label>
                            <input name="productName" class="form-control" type="text" value="${(productInfo.productName)!""}" />
                        </div>
                        <div class="form-group">
                            <label>Product Price:</label>
                            <input name="productPrice" class="form-control" type="text" value="${(productInfo.productPrice)!""}" />
                        </div>
                        <div class="form-group">
                            <label>Product Stock:</label>
                            <input name="productStock" class="form-control" type="number" value="${(productInfo.productStock)!""}" />
                        </div>

                        <div class="form-group">
                            <label>Product Description:</label>
                            <input name="productDescription" class="form-control" type="text" value="${(productInfo.productDescription)!""}" />
                        </div>

                        <div class="form-group">
                            <label>Product Category:</label>
                            <select name="categoryType" class="form-control" type="text" >
                            <#list categoryList as category>
                                <option value="${category.categoryType}"
                                        <#if (productInfo.categoryType)?? && productInfo.categoryType == category.categoryType>
                                        selected
                                        </#if>
                                   >${category.categoryName}
                                </option>
                            </#list>
                            </select>
                        </div>

                        <div class="form-group">
                            <label>Product Icon:</label>
                            <input name="productIcon" class="form-control" type="text" value="${(productInfo.productIcon)!''}" />
                        </div>

                        <div class="form-group">
                            <label>Preview:</label><br />
                            <#if (productInfo.productIcon)??>
                                <img height="120" width="120" src="${productInfo.productIcon}" alt="">
                            <#else>
                                <p>None</p>
                            </#if>
                        </div>

                        <br><button type="submit" class="btn btn-default active" >Save</button>

                        <input hidden type="text" name="productId" value="${(productInfo.productId)!''}">

                    </form>
                </div>
            </div>
        </div>
    </div>

</div>
</body>
</html>
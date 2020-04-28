<html>
<#include "../common/header.ftl">
<body>

<div id="wrapper" class="toggled">
    <#--siderbar-->
    <#include "../common/nav.ftl">

    <#-- main content-->
    <div id="page-content-wrapper">
        <div class="container">
            <#--top table-->
            <div class="row clearfix">
                <div class="col-md-5 column">
                    <table class="table table-bordered">
                        <thead>
                        <tr>
                            <th>OrderId</th>
                            <th>Total Amount</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td>${orderDTO.orderId}</td>
                            <td>${orderDTO.orderAmount}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>

            <#--middle table-->
            <div class="row clearfix">
                <div class="col-md-12 column">
                    <table class="table table-bordered">
                        <thead>
                        <tr>
                            <th>Product Id</th>
                            <th>Product Name</th>
                            <th>Unit Price</th>
                            <th>Quantity</th>
                            <th>Amount</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list orderDTO.orderDetailList as orderDetail>
                            <tr>
                                <td>${orderDetail.productId}</td>
                                <td>${orderDetail.productName}</td>
                                <td>${orderDetail.productPrice}</td>
                                <td>${orderDetail.productQuantity}</td>
                                <td>${orderDetail.productQuantity * orderDetail.productPrice}</td>
                            </tr>
                        </#list>
                        </tbody>
                    </table>
                </div>
            </div>

            <#--bottom button-->
            <div class="row clearfix">
                <div class="col-md-12 column">
                    <#if orderDTO.getOrderStatusEnum().code == 0>
                        <a href="/order/seller/order/finish?orderId=${orderDTO.orderId}" type="button" class="btn btn-default btn-primary">Finish</a>
                        <a href="/order/seller/order/cancel?orderId=${orderDTO.orderId}" type="button" class="btn btn-default btn-danger">Cancle</a>
                    </#if>
                </div>
            </div>

        </div>
    </div>

</div>


</body>
</html>


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
                                <th>Order Id</th>
                                <th>Name</th>
                                <th>Phone</th>
                                <th>Address</th>
                                <th>Amount</th>
                                <th>Order Status</th>
                                <th>Payment Status</th>
                                <th>Create Time</th>
                                <th colspan="2">Operations</th>
                            </tr>
                            </thead>
                            <tbody>
                            <#list orderDTOPage.content as orderDTO>
                                <tr>
                                    <td>${orderDTO.orderId}</td>
                                    <td>${orderDTO.buyerName}</td>
                                    <td>${orderDTO.buyerPhoneNumber}</td>
                                    <td>${orderDTO.buyerAddress}</td>
                                    <td>${orderDTO.orderAmount}</td>
                                    <td>${orderDTO.getOrderStatusEnum().message}</td>
                                    <td>${orderDTO.getPayStatusEnum().message}</td>
                                    <td>${orderDTO.createTime?string("yyyy-MM-dd HH:mm")}</td>
                                    <td><a href="/order/seller/order/details?orderId=${orderDTO.orderId}">Details</a></td>
                                    <td>
                                        <#if orderDTO.getOrderStatusEnum().code==0>
                                            <a href="/order/seller/order/cancel?orderId=${orderDTO.orderId}">Cancel</a>
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
                                <li><a href="/order/seller/order/list?page=${currentPage-1}&size=${size}">Prev</a></li>
                            </#if>

                            <#list 1..orderDTOPage.getTotalPages() as index>
                                <#if currentPage == index>
                                    <li class="disabled"><a href="#">${index}</a></li>
                                <#else>
                                    <li><a href="/order/seller/order/list?page=${index}&size=${size}">${index}</a></li>
                                </#if>
                            </#list>

                            <#if currentPage gte orderDTOPage.getTotalPages()>
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

<div class="modal fade" id="myModal" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <h4 class="modal-title" id="myModalLabel">
                    Reminder
                </h4>
            </div>
            <div class="modal-body">
                Hi, New Order Arrives!
            </div>
            <div class="modal-footer">
                <button onclick="location.reload()" type="button" class="btn btn-primary">Got it!</button>
            </div>
        </div>

    </div>

</div>

    <script src="https://cdn.bootcss.com/jquery/1.12.4/jquery.min.js"></script>
    <script src="https://cdn.bootcss.com/twitter-bootstrap/3.3.5/js/bootstrap.min.js"></script>

    <script>
        var websocket = null;
        if('WebSocket' in window){
            websocket = new WebSocket('ws://localhost:8080/order/webSocket');
        }else{
            alert('The Browser does not support WebSocket.');
        }

        websocket.onopen = function (event) {
            console.log('Connection Established');
        }

        websocket.onclose = function (event) {
            console.log('Connection Broken');
        }

        websocket.onmessage = function (event) {
            console.log('Message Received! '+ event.data);
            // pop up
            $('#myModal').modal('show');
        }

        websocket.onerror = function () {
            alert('WebSocket: Communication Error ');
        }

        websocket.onbeforeunload = function () {
            websocket.close();
        }

    </script>
    </body>
</html>
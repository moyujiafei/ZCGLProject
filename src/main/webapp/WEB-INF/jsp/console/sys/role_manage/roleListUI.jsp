<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML>
<html>

    <head>
        <meta charset="utf-8" />
        <title></title>
    </head>

    <body>
        <div id="roleListdialog">
            <table id="roleListDatagrid"></table>
            <div id="roleToolbar"><a id="insertRoleBtn" href="javascript:void(0);">新增</a></div>
        </div>
        <script type="text/javascript">
            var roleListOpt = {
            	newDialog: function(dialogId,url,title,param){
            		var dialogObj = $('<div id="' + dialogId + '"></div>');
					dialogObj.appendTo("body");
					$("#" + dialogId).dialog({
	                    href: getContextPath() + url,
	                    title: title,
	                    queryParams: param,
	                    width: 512,
	                    height: 300,
	                    modal: true,
	                    draggable: true,
	                    inline: true,
	                    onClose: function() {
	                        dialogObj.remove();// 关闭时remove对话框
	                    }
	                });
            	},
                update: function(index) {
                    var rowInfo = $("#roleListDatagrid").datagrid("getRows")[index];
                    roleListOpt.newDialog("updateRoleDialog","/console/role/updateRoleUI.do","编辑角色", {"currRoleId": rowInfo.id});
                },
                insert: function() {
                	roleListOpt.newDialog("insertRoleDialog","/console/role/insertRoleUI.do","添加角色");
                }
            };
            $(function() {
                $('#roleListdialog').dialog({
                    title: '角色管理',
                    width: 1024,
                    height: 600,
                    closed: false,
                    cache: false,
                    shadow: true, // 显示阴影
                    resizable: false, // 不允许改变大小
                    modal: true, // 模式化窗口，锁定父窗口
                    inline: true, // 是否在父容器中，若不在会出现很多BUG
                });
				// 新增按钮
	            $("#insertRoleBtn").linkbutton({
		           plain: true,
		           iconCls: 'icon-add',
		           onClick: function(){
			           roleListOpt.insert()
			       }
	            });
                $('#roleListDatagrid').datagrid({ // 数据表格
                    fit: true,
                    singleSelect: true,
                    pagination: true,
                    striped: true,
                    rownumbers: true,
                    toolbar: '#roleToolbar',
                    pageSize: '15',
                    pageList: ['15'],
                    url: getContextPath() + '/console/role/roleDatagrid.do',
                    onLoadSuccess: function() { // 加载完后将edit渲染为button
                        $('.edit_button').linkbutton({
                            iconCls: 'icon-edit',
                            height: 22,
                            plain: true,
                        });
                        $(".note").tooltip({
                            trackMouse: true,
                            onShow: function() {
                                $(this).tooltip('tip').css({
                                    width: '300',
                                    boxShadow: '1px 1px 3px #292929'
                                });
                            },
                        });

                    },
                    rowStyler: function(index, row) {
                        if (row.tybz == '1') {
                            return 'background-color:#ffb1b1;';
                        }
                    },
                    columns: [
                        [{
                            field: 'name',
                            title: '名称',
                            align: 'center',
                            width: '10%',
                            resizable: false,
                        }, {
                            field: 'tybz',
                            title: '状态',
                            align: 'center',
                            width: '10%',
                            resizable: false,
                            formatter: function(value, row, index) {
                                if (value == 1) {
                                    return '停用';
                                } else {
                                    return '激活';
                                }
                            }
                        }, {
                            field: 'privList',
                            title: '权限',
                            align: 'left',
                            width: '63%',
                            resizable: false,
                            formatter: function(value, row, index) {
                                var abValue = (value == null) ? "" : value;
                                var content = '<span title="' + value + '" class="note">' + abValue + '</span>';
                                return content;
                            }
                        }, {
                            field: 'edit',
                            title: '操作',
                            align: 'center',
                            width: '15%',
                            resizable: false,
                            formatter: function(value, row, index) {
                                return '<a  href="javascript:void(0);" class="edit_button" onclick="roleListOpt.update(' + index + ')">修改</a>';
                            }

                        }]
                    ]

                });
            });
        </script>
    </body>

</html>

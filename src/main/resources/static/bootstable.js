/*
Bootstable
 @description  Javascript library to make HMTL tables editable, using Bootstrap
 @version 1.1
 @author Tito Hinostroza
 @author dpozinen
*/
  "use strict";

  var params = null;
  var colsEdi = null;
  var newColHtml = '<div class="btn-group pull-right">'+
'<button id="bEdit" type="button" class="btn btn-sm btn-md btn-primary" onclick="rowEdit(this);">' +
'<i class="fas fa-edit"></i>'+
'</button>'+
'<button id="bElim" type="button" class="btn btn-sm btn-md btn-danger" onclick="rowElim(this);">' +
'<i class="fas fa-trash"></i>'+
'</button>'+
'<button id="bAcep" type="button" class="btn btn-sm btn-md btn-success" style="display:none;" onclick="rowAcep(this);">' +
'<i class="fas fa-check-circle"></i>'+
'</button>'+
'<button id="bCanc" type="button" class="btn btn-sm btn-md btn-warning" style="display:none;" onclick="rowCancel(this);">' +
'<i class="fas fa-window-close"></i>'+
'</button>'+
'</div>';
  var colEdicHtml = '<td name="buttons">'+newColHtml+'</td>';

  $.fn.SetEditable = function (options) {
    var defaults = {
        columnsEd: null,         //Index to editable columns. If null all td editables. Ex.: "1,2,3,4,5"
        $addButton: null,        //Jquery object of "Add" button
        onEdit: function ($row) {
                    var xhr = new XMLHttpRequest();
                    var url = "/order/edit";
                    var token = $("meta[name='_csrf']").attr("content");
                    var header = $("meta[name='_csrf_header']").attr("content");

                    var order = {};
                    var id = $row.find('div').each(function() {
                        var id = $(this).attr('id');
                        var value = $(this).text();
                        if (id != undefined && id != 'dueDate' && id != 'createdDate') {
                            if (id.indexOf('State') >= 0) {
                                order[id] = value.replace(' ', '_');
                            } else {
                                order[id] = value;
                            }
                        }
                    });

                    xhr.onload = function () {
                        if (this.status != 200) {
                            alert('Could not process that request, please try again');
                            location.reload();
                        }
                    };

                    xhr.open("POST", url, true);
                    xhr.setRequestHeader("Content-Type", "application/json");
                    xhr.setRequestHeader(header, token);

                    var data = JSON.stringify(order);
                    xhr.send(data);
                  },   //Called after edition
		onBeforeDelete: function() {}, //Called before deletion
        onDelete: function() {}, //Called after deletion
        onAdd: function() {},     //Called when added a new row
		$addColButton : function() {}, //rowAddNewCol
		onAddCol : function() {} // Called after adding a column
    };
    params = $.extend(defaults, options);
    this.find('thead tr').append('<th name="buttons"></th>');
    this.find('tbody tr').append(colEdicHtml);
	var $tabedi = this;

    if (params.$addButton != null) {

        params.$addButton.click(function() {
            rowAddNew($tabedi.attr("id"));
        });
    }

    if (params.columnsEd != null) {
        colsEdi = params.columnsEd.split(',');
    }
  };

function IterarCamposEdit($cols, tarea) {

    var n = 0;
    $cols.each(function() {
        n++;
        if ($(this).attr('name')=='buttons') return;
        if (!EsEditable(n-1)) return;
        tarea($(this));
    });

    function EsEditable(idx) {

        if (colsEdi==null) {
            return true;
        } else {

            for (var i = 0; i < colsEdi.length; i++) {
              if (idx == colsEdi[i]) return true;
            }
            return false;
        }
    }
}
function FijModoNormal(but) {
    $(but).parent().find('#bAcep').hide();
    $(but).parent().find('#bCanc').hide();
    $(but).parent().find('#bEdit').show();
    $(but).parent().find('#bElim').show();
    var $row = $(but).parents('tr');
    $row.attr('id', '');
}
function FijModoEdit(but) {
    $(but).parent().find('#bAcep').show();
    $(but).parent().find('#bCanc').show();
    $(but).parent().find('#bEdit').hide();
    $(but).parent().find('#bElim').hide();
    var $row = $(but).parents('tr');
    $row.attr('id', 'editing');
}
function ModoEdicion($row) {
    if ($row.attr('id')=='editing') {
        return true;
    } else {
        return false;
    }
}
function rowAcep(but) {

    var $row = $(but).parents('tr');
    var $cols = $row.find('td');
    if (!ModoEdicion($row)) return;

    IterarCamposEdit($cols, function($td) {
      var cont = $td.find('input').val();
      var id = $td.find('div').attr('id');
      var div = '<div class="animated fadeIn my-2" id="'+id+'">' + cont + '</div>';
      $td.html(div);
    });
    FijModoNormal(but);
    params.onEdit($row);
}
function rowCancel(but) {

    var $row = $(but).parents('tr');
    var $cols = $row.find('td');
    if (!ModoEdicion($row)) return;

    IterarCamposEdit($cols, function($td) {
      var cont = $td.find('input').val();
      var id = $td.find('div').attr('id');
      var div = '<div class="animated fadeIn my-2" id="'+id+'">' + cont + '</div>';
      $td.html(div);
    });
    FijModoNormal(but);
}
function rowEdit(but) {
    var $row = $(but).parents('tr');
    var $cols = $row.find('td');
    if (ModoEdicion($row)) return;

    IterarCamposEdit($cols, function($td) {
        var cont = $td.text().trim();
        var id = $td.find('div').attr('id');
        var input = '<div class="md-form my-0" id="'+id+'"> <input class="form-control animated fadeIn form-control-sm"  value="' + cont + '"> </div>';
        $td.html(input);
    });
    FijModoEdit(but);
}
function rowElim(but) {
    var $row = $(but).parents('tr');
    params.onBeforeDelete($row);
    $row.remove();
    params.onDelete();
}


function rowAddNew(tabId) {
var $tab_en_edic = $("#" + tabId);
    var $filas = $tab_en_edic.find('tbody tr');
    if ($filas.length==0) {

        var $row = $tab_en_edic.find('thead tr');
        var $cols = $row.find('th');

        var htmlDat = '';
        $cols.each(function() {
            if ($(this).attr('name')=='buttons') {

                htmlDat = htmlDat + colEdicHtml;
            } else {
                htmlDat = htmlDat + '<td></td>';
            }
        });
        $tab_en_edic.find('tbody').append('<tr>'+htmlDat+'</tr>');
    } else {

        var $ultFila = $tab_en_edic.find('tr:last');
        $ultFila.clone().appendTo($ultFila.parent());
        $ultFila = $tab_en_edic.find('tr:last');
        var $cols = $ultFila.find('td');
        $cols.each(function() {
            if ($(this).attr('name')=='buttons') {

            } else {
                $(this).html('');
            }
        });
    }
	params.onAdd();
}

function rowAddNewCol(tabId) {
var $tab_en_edic = $("#" + tabId);
    var $filas = $tab_en_edic.find('tbody tr');
    if ($filas.length==0) {
		alert('NO cols found');
    } else {
		$filas.each(function() {
			$(this).find('td:last').before('<td></td>');
		});
    }

	var $row = $tab_en_edic.find('thead tr');
	var $cols = $row.find('th');

	var colname=prompt("Please enter column name","new column");
	if (colname!=null){
       var alertcolname = "Column  " + colname + "  will be created ";
    }
	else{
		colname = "new column";
	}

	$row.find('th:last').prev().before('<th>'+ colname + '</th>');

	params.onAddCol();
}

function TableToCSV(tabId, separator) {
    var datFil = '';
    var tmp = '';
	var $tab_en_edic = $("#" + tabId);
    $tab_en_edic.find('tbody tr').each(function() {

        if (ModoEdicion($(this))) {
            $(this).find('#bAcep').click();
        }
        var $cols = $(this).find('td');
        datFil = '';
        $cols.each(function() {
            if ($(this).attr('name')=='buttons') {

            } else {
                datFil = datFil + $(this).html() + separator;
            }
        });
        if (datFil!='') {
            datFil = datFil.substr(0, datFil.length-separator.length);
        }
        tmp = tmp + datFil + '\n';
    });
    return tmp;
}

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
var newColHtml = '<div class="btn-group pull-right">' +
    '<button id="bEdit" type="button" class="btn btn-sm btn-md btn-primary" onclick="startEdit(this);">' +
    '<i class="fas fa-edit"></i>' +
    '</button>' +
    '<button id="bElim" type="button" class="btn btn-sm btn-md btn-danger" onclick="removeRow(this);">' +
    '<i class="fas fa-trash"></i>' +
    '</button>' +
    '<button id="bAcep" type="button" class="btn btn-sm btn-md btn-success" style="display:none;" onclick="acceptEdit(this);">' +
    '<i class="fas fa-check-circle"></i>' +
    '</button>' +
    '<button id="bCanc" type="button" class="btn btn-sm btn-md btn-warning" style="display:none;" onclick="cancelEdit(this);">' +
    '<i class="fas fa-window-close"></i>' +
    '</button>' +
    '</div>';
var colEdicHtml = '<td name="buttons">' + newColHtml + '</td>';

$.fn.SetEditable = function (options) {
    var defaults = {
        editableCols: "1,2,5,6",  //Index to editable columns. If null all td editables. Ex.: "1,2,3,4,5"
        $addButton: null,        //Jquery object of "Add" button
        onEdit: function ($row) {
            var xhr = new XMLHttpRequest();
            var url = "/order/edit";
            var token = $("meta[name='_csrf']").attr("content");
            var header = $("meta[name='_csrf_header']").attr("content");

            var order = {};
            $row.find('div').each(function () {
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
        onBeforeDelete: function () {
            return confirm("Error writing toConfirm order delete?");
        }, //Called before deletion
        onDelete: function ($row) {
            var xhr = new XMLHttpRequest();
            var token = $("meta[name='_csrf']").attr("content");
            var header = $("meta[name='_csrf_header']").attr("content");

            var id = $row.find('div[id="id"]').text().trim();
            var url = "/order/delete/" + id;

            xhr.onload = function () {
                if (this.status != 200) {
                    alert('Could not process that request, please try again');
                    location.reload();
                }
            };

            xhr.open("DELETE", url, true);
            xhr.setRequestHeader("Content-Type", "application/json");
            xhr.setRequestHeader(header, token);

            xhr.send();
        }, //Called after deletion
        onAdd: function () { },     //Called when added a new row
        $addColButton: function () { }, //rowAddNewCol
        onAddCol: function () { } // Called after adding a column
    };
    params = $.extend(defaults, options);
    this.find('thead tr').append('<th name="buttons"></th>');
    this.find('tbody tr').append(colEdicHtml);
    var $tabedi = this;

    if (params.$addButton != null) {
        params.$addButton.click(function () {
            rowAddNew($tabedi.attr("id"));
        });
    }

    if (params.editableCols != null) {
        colsEdi = params.editableCols.split(',');
    }
};

function iterateEditableCols($cols, tarea) {

    var n = 0;
    $cols.each(function () {
        n++;
        if ($(this).attr('name') == 'buttons') return;
        if (!EsEditable(n - 1)) return;
        tarea($(this));
    });

    function EsEditable(idx) {

        if (colsEdi == null) {
            return true;
        } else {

            for (var i = 0; i < colsEdi.length; i++) {
                if (idx == colsEdi[i]) return true;
            }
            return false;
        }
    }
}

function toNormalMode(but) {
    $(but).parent().find('#bAcep').hide();
    $(but).parent().find('#bCanc').hide();
    $(but).parent().find('#bEdit').show();
    $(but).parent().find('#bElim').show();
    var $row = $(but).parents('tr');
    $row.attr('id', '');
}

function toEditMode(but) {
    $(but).parent().find('#bAcep').show();
    $(but).parent().find('#bCanc').show();
    $(but).parent().find('#bEdit').hide();
    $(but).parent().find('#bElim').hide();
    var $row = $(but).parents('tr');
    $row.attr('id', 'editing');
}

function isEditMode($row) {
    if ($row.attr('id') == 'editing') {
        return true;
    } else {
        return false;
    }
}

function acceptEdit(but) {

    var $row = $(but).parents('tr');
    var $cols = $row.find('td');
    if (!isEditMode($row)) return;

    iterateEditableCols($cols, function ($td) {
        var id = $td.find('div').attr('id');

        var cont;
        if (id == 'workState' || id == 'payState') {
            cont = $td.find('select').val().replace('_', ' ');
        } else {
            cont = $td.find('input').val();
        }

        var div = '<div class="animated fadeIn my-2" id="' + id + '">' + cont + '</div>';
        $td.html(div);
    });

    toNormalMode(but);
    params.onEdit($row);
}

function cancelEdit(but) {
    var $row = $(but).parents('tr');
    var $cols = $row.find('td');
    if (!isEditMode($row)) return;

    iterateEditableCols($cols, function ($td) {
        var id = $td.find('div').attr('id');

        var cont;
        if (id == 'workState' || id == 'payState') {
            cont = $('option[selected]').val();
        } else {
            cont = $td.find('input').attr('value')
        }

        var div = '<div class="animated fadeIn my-2" id="' + id + '">' + cont + '</div>';
        $td.html(div);
    });
    toNormalMode(but);
}

function startEdit(but) {

    $('button#bCanc').each(function() {
        cancelEdit($(this));
    })

    var $row = $(but).parents('tr');
    var $cols = $row.find('td');
    if (isEditMode($row)) return;

    iterateEditableCols($cols, function ($td) {
        var cont = $td.text().trim();
        var id = $td.find('div').attr('id');

        if (id == 'payState') {
            makePayStateCell($td);
        } else if (id == 'workState') {
            makeWorkStateCell($td);
        } else {
            var input = '<div class="md-form my-0" id="' + id + '"> <input class="form-control animated fadeIn form-control-sm"  value="' + cont + '"> </div>';
            $td.html(input);
        }
    });
    toEditMode(but);
}

function removeRow(but) {
    var $row = $(but).parents('tr');
    if (params.onBeforeDelete($row)) {
        $row.fadeOut(300, function() { $(this).remove(); });
        params.onDelete($row);
    }
}

function rowAddNew(tabId) {
    var $tab_en_edic = $("#" + tabId);
    var $filas = $tab_en_edic.find('tbody tr');
    if ($filas.length == 0) {

        var $row = $tab_en_edic.find('thead tr');
        var $cols = $row.find('th');

        var htmlDat = '';
        $cols.each(function () {
            if ($(this).attr('name') == 'buttons') {

                htmlDat = htmlDat + colEdicHtml;
            } else {
                htmlDat = htmlDat + '<td></td>';
            }
        });
        $tab_en_edic.find('tbody').append('<tr>' + htmlDat + '</tr>');
    } else {

        var $ultFila = $tab_en_edic.find('tr:last');
        $ultFila.clone().appendTo($ultFila.parent());
        $ultFila = $tab_en_edic.find('tr:last');
        var $cols = $ultFila.find('td');
        $cols.each(function () {
            if ($(this).attr('name') == 'buttons') {

            } else {
                $(this).html('');
            }
        });
    }
    params.onAdd();
}

function makeWorkStateCell($td) {
    var select = 
    `
        <div class="md-form my-1 animated fadeIn" id="workState">
            <select class="mdb-select workState colorful-select input-sm dropdown-primary" data-visible-options="2">
                <option value="QUEUED">QUEUED</option>
                <option value="DONE">DONE</option>
                <option value="IN PROGRESS">IN PROGRESS</option>
                <option value="DELAYED">DELAYED</option>
            </select>
        </div>
    `;
    var text = $td.find('div').text().trim();
    $td.html(select);

    $('option[value="'+text+'"]').attr('selected', 'selected');
    $('.workState').materialSelect();
}

function makePayStateCell($td) {
    var select =
    `
        <div class="md-form my-0 animated fadeIn scrollbar square scrollbar-lady-lips thin" id="payState">
            <select class="mdb-select payState colorful-select input-sm dropdown-primary">
                <option value="PAYED">PAYED</option>
                <option value="NOT PAYED">NOT PAYED</option>
            </select>
        </div>
    `;
    var text = $td.find('div').text().trim();
    $td.html(select);

    $('option[value="'+text+'"]').attr('selected', 'selected');
    $('.payState').materialSelect();
}

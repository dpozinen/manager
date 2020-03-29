
function getChartData() {
  var xhr = new XMLHttpRequest();
  var token = $("meta[name='_csrf']").attr("content");
  var header = $("meta[name='_csrf_header']").attr("content");

  var period = $('#period').val();
  var periodLength = $('#periodLength').val();

  var url = "/order/count?period=" + period + "&periodLength=" + periodLength;

  xhr.onload = function () {
    let arr;
    if (this.status != 200) {
      arr = [];
    } else {
      var response = JSON.parse(xhr.response);
      drawChart(response, period, periodLength);
    }
  };

  xhr.open("GET", url, true);
  xhr.setRequestHeader(header, token);

  xhr.send();
}

var chart;

function drawChart(response, period, periodLength) {

  var labels = Object.keys(response);
  var data = Object.values(response);

  var min = Math.min(...data);
  var max = Math.max(...data);

  var total = data.reduce((a, b) => a + b, 0);
  $('#orders-this-period').text("Total orders: " + total);

  var ctxL = document.getElementById("lineChart").getContext('2d');

  if (chart != null) { chart.destroy(); }

  chart = new Chart(ctxL, {
    type: 'line',
    data: {
      labels: labels,
      datasets: [
        {
          fill: false,
          borderColor: "#673ab7",
          pointBackgroundColor: "#673ab7",
          data: data
        }
      ]
    },
    options: {
      responsive: false,
      legend: {
        display: false
      },
      elements: {
        line: {
          tension: 0.0
        }
      },
      scales: {
        xAxes: [{
          gridLines: {
            display: false,
          },
          ticks: {
            padding: 15,
            height: 30
          }
        }],
        yAxes: [{
          gridLines: {
            drawBorder: false
          },
          ticks: {
            maxTicksLimit: 5,
            padding: 15,
            min: min,
            max: max
          }
        }]
      }
    }
  });

}

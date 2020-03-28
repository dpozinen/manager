
function getChartData() {
  var xhr = new XMLHttpRequest();
  var token = $("meta[name='_csrf']").attr("content");
  var header = $("meta[name='_csrf_header']").attr("content");

  var url = "/order/count?period=month;

  xhr.onload = function () {
    let arr;
    if (this.status != 200) {
      arr = [];
    } else {
      var response = JSON.parse(xhr.response);

      for ()

      arr = [ response[1], response[2], response['W3'], response['W4'] ];
      drawChart(arr);
    }
  };

  xhr.open("GET", url, true);
  xhr.setRequestHeader(header, token);

  xhr.send();
}

function drawChart($data) {

  var ctxL = document.getElementById("lineChart").getContext('2d');
  var myLineChart = new Chart(ctxL, {
    type: 'line',
    data: {
      labels: [W1, W2, W3, W4],
      datasets: [
        {
          fill: false,
          borderColor: "#673ab7",
          pointBackgroundColor: "#673ab7",
          data: $data
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
            min: 880,
            max: 890
          }
        }]
      }
    }
  });

}

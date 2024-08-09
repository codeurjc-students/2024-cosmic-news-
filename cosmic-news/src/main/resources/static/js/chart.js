google.charts.load('current', { 'packages': ['corechart'] });
google.charts.setOnLoadCallback(drawChart);

function drawChart() {
    var data = [['Aptitudes', 'Cantidad']];
    var attitudes = document.querySelectorAll('.attitude');
    attitudes.forEach(function(element) {
        var name = element.getAttribute('data-name');
        var value = parseInt(element.value);
        data.push([name, value]);
    });

    var dataTable = google.visualization.arrayToDataTable(data);

    var options = {
        title: 'Quizzes completados',
    };

    var chart = new google.visualization.PieChart(document.getElementById('piechart'));
    chart.draw(dataTable, options);
}
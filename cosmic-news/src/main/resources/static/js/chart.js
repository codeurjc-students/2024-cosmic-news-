google.charts.load('current', { 'packages': ['corechart'] });
google.charts.setOnLoadCallback(drawChart);

function drawChart() {
    var data = [['Aptitudes', 'Cantidad']];
    var quizzes = document.querySelectorAll('.quizz');
    quizzes.forEach(function(element) {
        var name = element.getAttribute('data-name');
        var value = parseInt(element.value);
        data.push([name, value]);
    });

    var dataTable = google.visualization.arrayToDataTable(data);

    var options = {
        title: 'Quizzes completados',
        width: '100%',
        height: '100%',
        tooltip: { isHtml: true, textStyle: { fontSize: 16 }, showColorCode: true, ignoreBounds: true, isHtml: true, forceIFrame: true },
        pieSliceText: 'value',
    };

    var chart = new google.visualization.PieChart(document.getElementById('piechart'));
    chart.draw(dataTable, options);
}
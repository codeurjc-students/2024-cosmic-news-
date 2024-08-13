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
        tooltip: { 
            isHtml: true, 
            textStyle: { fontSize: 16 }, 
            showColorCode: true, 
            ignoreBounds: true, 
            isHtml: true, 
            forceIFrame: true 
        },
        pieSliceText: 'value',
        chartArea: {
            left: '10%',
            top: '10%',
            width: '80%',
            height: '80%'
        },
        width: '100%',
        height: '100%',
        responsive: true,
    };

    var chart = new google.visualization.PieChart(document.getElementById('piechart'));
    chart.draw(dataTable, options);
}

window.addEventListener('resize', drawChart);
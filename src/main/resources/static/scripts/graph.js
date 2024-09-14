const Chart = toastui.Chart;

        $(document).ready(function () {
            // 차트 엘리먼트
            const $chart = $('#chart');

            // 차트 생성 함수
            function renderChart(data) {
                const chartData = {
                    categories: ['Transaction'],
                    series: data.map(item => ({
                        name: item.categoryName,
                        data: item.amount
                    }))
                };

                const options = {
                    chart: { width: 800, height: 500 },
                    series: {
                        dataLabels: {
                            visible: true,
                            pieSeriesName: {
                                visible: true,
                                anchor: 'outer'
                            }
                        }
                    }
                };

                // 기존 차트 제거
                $chart.empty();

                // 새 차트 생성
                const chart = toastui.Chart.pieChart({
                    el: $chart[0],
                    data: chartData,
                    options: options
                });
            }

            // 확인 버튼 클릭 이벤트
            $('#confirmDate').on('click', function () {
                const startDate = $('#startDate').val();
                const endDate = $('#endDate').val();

                if (!startDate || !endDate) {
                    alert('시작 날짜와 마지막 날짜를 입력해주세요.');
                    return;
                }

                // AJAX로 서버에서 데이터 가져오기
                $.ajax({
                    url: `transactions/graph?startDate=${startDate}&endDate=${endDate}`,
                    method: 'GET',
                    success: function (response) {
                        renderChart(response);
                    },
                    error: function (error) {
                        console.error('데이터 가져오기 중 오류 발생:', error);
                        alert('데이터를 가져오는 중 문제가 발생했습니다.');
                    }
                });
            });
        });
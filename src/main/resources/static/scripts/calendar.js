$(document).ready(function () {
    const Calendar = tui.Calendar;
    const calendar = new Calendar('#calendar', {
      defaultView: 'month',
      timezone: {
        zones: [
          { timezoneName: 'Asia/Seoul', displayLabel: 'Seoul' }
        ]
      },
      calendars: [
        { id: 'EXPENSE', name: '지출' },
        { id: 'INCOME', name: '수입' }
      ],
    });

    // 캘린더에 설정할 캘린더 타입
    calendar.setCalendars([
      { id: 'EXPENSE', name: '지출', color: '#f20707', backgroundColor: '#ffffff', borderColor: '#ffffff' },
      { id: 'INCOME', name: '수입', color: '#1302fa', backgroundColor: '#ffffff', borderColor: '#ffffff' },
    ]);

    // 날짜 버튼 클릭 핸들러
    function onDateButtonClick(action) {
      calendar[action]();
      updateCalendar();
    }

    // 캘린더 업데이트
    function updateCalendar() {
      const yearMonth = moment(calendar.getDate().getTime()).format('YYYY-MM');
      displayRenderRange();
      fetchMonthlyEvents(yearMonth);
      fetchMonthlySummary(yearMonth);
    }

    // 달력 범위 표시 업데이트
    function displayRenderRange() {
      $('.navbar--range').text(moment(calendar.getDate().getTime()).format('YYYY년 MM월'));
    }

    // 월별 이벤트 가져오기 (AJAX)
    function fetchMonthlyEvents(yearMonth) {
      $.ajax({
        url: `http://localhost:8080/transactions/monthly/${yearMonth}`,
        method: 'GET',
        dataType: 'json',
        success: function (data) {
          calendar.clear();
          const events = data.map((item, index) => {
            return {
              id: `dailySummary${index + 1}`,
              calendarId: item.status,
              title: item.status === 'EXPENSE' ? `- ${item.total}` : `+ ${item.total}`,
              start: item.date,
              end: item.date,
              category: "allday"
            };
          });
          calendar.createEvents(events);
        },
        error: function (error) {
          console.error('AJAX 오류:', error);
        }
      });
    }

    // 월별 집계 가져오기 (AJAX)
    function fetchMonthlySummary(yearMonth) {
      $.ajax({
        url: `http://localhost:8080/transactions/monthlySummary/${yearMonth}`,
        method: 'GET',
        dataType: 'json',
        success: function (data) {
          $('#total').html(`
            <h2>월별 지출: ${data.totalExpense.toLocaleString()}</h2>
            <h2>월별 수입: ${data.totalIncome.toLocaleString()}</h2>
          `);
        },
        error: function (error) {
          console.error('AJAX 오류:', error);
        }
      });
    }

    // 초기 화면 로드
    updateCalendar();

    // 버튼 이벤트 설정
    $('.prev').on('click', function () { onDateButtonClick('prev'); });
    $('.next').on('click', function () { onDateButtonClick('next'); });
    $('.today').on('click', function () { onDateButtonClick('today'); });
  });
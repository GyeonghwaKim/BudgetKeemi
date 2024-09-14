//날짜 포맷
function formatDate(isoString) {
    const cleanIsoString = isoString.split('.')[0];
    const date = new Date(cleanIsoString);

    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');

    return `${year}-${month}-${day}`;
  }

  $(document).ready(function () {
    $.ajax({
      url: 'http://localhost:8080/budgets',
      method: 'GET',
      success: function (budgets) {
        var budgetContainer = $('.budget-container');
        budgetContainer.empty();

        budgets.forEach(function (budget) {
          var startDate = new Date(budget.startDate).toISOString().split('T')[0].replace(/-/g, '.');
          var endDate = new Date(budget.endDate).toISOString().split('T')[0].replace(/-/g, '.');

          console.log(budget.goalAmount)

          var budgetCard = `

          <div class="text-center mb-4">
          <h4>나의 <span>${budget.categoryName}</span> 예산</h4>
          <h6> 목표 예산 - <span class="goal-amount">${budget.goalAmount.toLocaleString()}</span>원</h6>
          <h6> 현재 사용된 금액 - <span class="used-amount">${budget.useAmount.toLocaleString()}</span>원</h6>
          <p>예산 기간: <span>${startDate} ~ ${endDate}</span></p>
          </div>
          `;

          budgetContainer.append(budgetCard);
        });
      },
      error: function (error) {
        console.error("예산 데이터를 불러오는 중 오류 발생:", error);
        alert("예산 데이터를 불러오는 중 오류가 발생했습니다.");
      }
    });

    // 계좌 데이터를 가져오는 AJAX 요청
    $.ajax({
      url: 'http://localhost:8080/accounts/active',
      method: 'GET',
      success: function (accounts) {
        var accountContainer = $('.account-container');
        accountContainer.empty();

        accounts
          .filter(account => account.active !== 'N')
          .forEach(function (account) {
            const formattedDate = formatDate(account.createDate);

            var accountItem = `
              <div class="account-card p-3 shadow-sm" id="budget-${account.id}">
                <h5>${account.name}</h5>
                <h6>${account.status}</h6>
                <h6>${formattedDate}</h6>            
                <span>${account.balance.toLocaleString()} 원</span>
              </div>
            `;
            accountContainer.append(accountItem);
          });
      },
      error: function (error) {
        console.error("계좌 데이터를 불러오는 중 오류 발생:", error);
      }
    });

    // 설정 버튼 클릭
    $('#settingsButton').on('click', function () {
      window.location.href = '/profile'; // 설정 페이지로 이동
    });

    //버튼 클릭
    // 거래 내역 보기 버튼 클릭
    $('#viewTransaction').on('click', function () {
      window.location.href = '/transactionPage'; // 거래 내역 페이지로 이동
    });

    // 차트 모아 보기 버튼 클릭 (현재 기능 미구현)
    $('#viewExpenseGraph').on('click', function () {
      window.location.href = '/graphPage';
    });

    // 예산 관리 하기 버튼 클릭
    $('#manageBudget').on('click', function () {
      window.location.href = '/budgetPage'; // 예산 관리 페이지로 이동
    });

    // 계좌 관리 하기 버튼 클릭
    $('#manageAccount').on('click', function () {
      window.location.href = '/accountPage'; // 계좌 관리 페이지로 이동
    });
  });
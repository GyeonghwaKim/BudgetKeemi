function formatDateTime(dateTimeString) {
    var date = new Date(dateTimeString);

    var year = date.getFullYear();
    var month = String(date.getMonth() + 1).padStart(2, '0');
    var day = String(date.getDate()).padStart(2, '0');

    var hours = String(date.getHours()).padStart(2, '0');
    var minutes = String(date.getMinutes()).padStart(2, '0');
    var seconds = String(date.getSeconds()).padStart(2, '0');

    // 'YYYY-MM-DD HH:mm:ss' 형식으로 변환
    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
  }


  $(document).ready(function () {
    $('#useCustomDate').change(function () {
      if (this.checked) {
        $('#transactionDateTime').prop('disabled', false);  // 체크되면 날짜 및 시간 필드 활성화
      } else {
        $('#transactionDateTime').prop('disabled', true);   // 체크 해제되면 날짜 및 시간 필드 비활성화
      }
    });


    //select에 들어갈 카테고리 목록
    $.ajax({
      url: 'http://localhost:8080/categories/active',
      method: 'GET',
      success: function (categories) {
        var categorySelect = $('#category');
        categorySelect.empty();
        categories.forEach(function (categories) {
          var option = $('<option></option>').val(categories.id).text(categories.status + ' - ' + categories.name);
          categorySelect.append(option);
        });
      },
      error: function (error) {
        console.error("카테고리 데이터를 불러오는 중 오류 발생:", error);
      }
    });

    //select에 들어갈 계좌 목록
    $.ajax({
      url: 'http://localhost:8080/accounts/active',
      method: 'GET',
      success: function (accounts) {
        var accountSelect = $('#account');
        accountSelect.empty();

        accounts.forEach(function (account) {
          accountSelect.append(`<option value="${account.id}">${account.status} - ${account.name}</option>`);
        });
      },
      error: function (error) {
        console.error("계좌 유형 데이터를 불러오는 중 오류 발생:", error);
      }
    });

    //거래내역 get
    $.ajax({
      url: 'http://localhost:8080/transactions', // 실제 서버 URL로 변경 필요
      method: 'GET',
      success: function (transactions) {
        var transactionContainer = $('.transaction-card-container'); // 거래 내역을 표시할 컨테이너 선택

        // 거래 내역 리스트 초기화
        transactionContainer.empty();

        transactions.forEach(function (transaction) {

          var isDisabled = transaction.active === 'N' ? 'disabled' : '';
          var isCancel = transaction.active === 'N' ? 'del' : '';

          // 삭제 버튼을 추가할 조건: categoryStatus가 EXPENSE일 경우에만
          var deleteButton = transaction.categoryStatus === 'EXPENSE'
            ? `<button class="btn btn-danger btn-sm float-end delete-btn ${isDisabled}" data-id="${transaction.id}">취소</button>`
            : '';

          var transactionCard = `
            <div class="account-card p-3 shadow-sm ${isCancel}">
              ${deleteButton}
              <h5 class="${isCancel}">${transaction.categoryName}</h5>
              <h6 class="${isCancel}">${formatDateTime(transaction.transacDate)}</h6>
              <span class="${isCancel} ${transaction.categoryStatus == "INCOME" ? 'income' : 'expense'}">
                ${transaction.categoryStatus == "INCOME" ? '+' : '-'} ${transaction.amount.toLocaleString()} 원
              </span>
              ${transaction.description ? `<p> - ${transaction.description}</p>` : ''}
            </div>
          `;

          transactionContainer.append(transactionCard);
        });
      },
      error: function (error) {
        console.error("거래 내역을 불러오는 중 오류 발생:", error);
        alert("거래 내역을 불러오는 중 오류가 발생했습니다.");
      }
    });

    //거래내역 post
    $('#submitTransac').click(function () {
      // 입력된 값을 가져옴
      var category = $('#category').val();
      var account = $('#account').val();
      var amount = $('#amount').val();
      var description = $('#description').val();
      var useCustomDate = $('#useCustomDate').is(':checked');
      var transactionDateTime;

      // 입력값 검증
      if (!account) {
        alert("계좌를 선택하세요.");
        return;
      }

      if (!amount || isNaN(amount) || amount < 0) {
        alert("유효한 금액을 입력하세요.");
        return;
      }

      if (!category) {
        alert("카테고리를 선택하세요.");
        return;
      }

      // 거래 날짜 및 시간 설정: 사용자 지정 날짜 또는 현재 날짜
      if (useCustomDate) {
        transactionDateTime = $('#transactionDateTime').val();
        if (!transactionDateTime) {
          alert("거래 날짜와 시간을 선택하세요.");
          return;
        }
      } else {
        var now = new Date();
        transactionDateTime = now.toISOString().slice(0, 19);  // 현재 날짜와 시간 'YYYY-MM-DDTHH:MM:SS' 형식
      }

      // 요청 데이터 생성
      var transactionData = {
        accountId: account,
        categoryId: category,
        amount: amount,
        description: description,
        transacDate: transactionDateTime  // 거래 날짜와 시간 추가
      };

      // 서버로 POST 요청 전송
      $.ajax({
        url: 'http://localhost:8080/transactions',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(transactionData),
        success: function (response) {
          alert("거래가 성공적으로 저장되었습니다.");
          location.reload();
        },
        error: function (xhr, status, error) {
          console.error('거래 저장 중 오류 발생:', error);

          if (xhr.status === 400) {
            alert(xhr.responseText)
          } else if (xhr.status === 403) {
            alert(xhr.responseText)
          }
          else {
            alert("거래 저장 중 오류가 발생했습니다.");
          }

        }
      });
    });

    //거래내역 삭제
    $('.transaction-card-container').on('click', '.delete-btn', function () {

      var transactionId = $(this).data('id');

      if (confirm("이 거래 내역을 취소하시겠습니까?")) {
        $.ajax({
          url: 'http://localhost:8080/transactions/' + transactionId,
          method: 'DELETE',
          success: function (response) {
            console.log('거래 내역 취소 성공:', response);
            alert("거래 내역이 성공적으로 취소되었습니다!");
            location.reload();

          },
          error: function (xhr, status, error) {
            console.error('거래 취소 중 오류 발생:', error);
            if (xhr.status === 403) {
              alert(xhr.responseText);
            } else {
              alert("거래 취소 중 오류가 발생했습니다.");
            }
          }
        })
      }


    });


  });
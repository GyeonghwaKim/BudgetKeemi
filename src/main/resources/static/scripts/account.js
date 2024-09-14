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

    //account type
    $.ajax({
      url: 'http://localhost:8080/accounts/accountType',
      method: 'GET',
      success: function (accountTypes) {
        var accountTypeSelect = $('#accountType');
        accountTypeSelect.empty();

        accountTypes.forEach(function (type) {
          accountTypeSelect.append(`<option value="${type}">${type}</option>`);
        });
      },
      error: function (error) {
        console.error("계좌 유형 데이터를 불러오는 중 오류 발생:", error);
      }
    });
    //account type 수정 모달
    $.ajax({
      url: 'http://localhost:8080/accounts/accountType',
      method: 'GET',
      success: function (accountTypes) {
        var accountTypeSelect = $('#editAccountType');
        accountTypeSelect.empty();

        accountTypes.forEach(function (type) {
          accountTypeSelect.append(`<option value="${type}">${type}</option>`);
        });
      },
      error: function (error) {
        console.error("계좌 유형 데이터를 불러오는 중 오류 발생:", error);
      }
    });


    //계좌 생성
    $('#submintAccount').click(function () {
      var accountName = $('#accountName').val();
      var balance = $('#balance').val();
      var accountType = $('#accountType').val();

      //입력값 검증
      if (!accountName) {
        alert("계좌 이름을 선택하세요.");
        return;
      }

      if (!balance || isNaN(balance) || balance < 0) {
        alert("유효한 금액을 입력하세요.");
        return;
      }

      if (!accountType) {
        alert("계좌 유형을 선택하세요.");
        return;
      }

      // 요청 데이터 생성
      var accountData = {
        name: accountName,
        balance: balance,
        status: accountType
      };

      $.ajax({
        url: 'http://localhost:8080/accounts',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(accountData),
        success: function (response) {
          alert("계좌가 성공적으로 생성되었습니다.");
          location.reload();
        },
        error: function (xhr, status, error) {
          console.error('계좌 생성 중 오류 발생:', error);
          alert("계좌 생성 중 오류가 발생했습니다.");
        }
      });
    });


    // 활성화 계좌 데이터를 가져오는 AJAX 요청
    $.ajax({
      url: 'http://localhost:8080/accounts/active',
      method: 'GET',
      success: function (accounts) {
        var accountContainer = $('.activeAccount-card-container');
        accountContainer.empty();

        // 계좌 데이터 렌더링
        accounts.forEach(function (account) {
          const formattedDate = formatDate(account.createDate);

          var accountItem = `
      <div class="account-card p-3 shadow-sm " id="account-${account.id}">
        <button class="btn btn-danger btn-sm float-end delete-btn" data-id="${account.id}">삭제</button>
        <button class="btn btn-primary btn-sm float-end edit-btn" data-id="${account.id}" data-name="${account.name}" data-balance="${account.balance}" data-type="${account.status}"}>수정</button>
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

    // 비활성화 계좌 데이터를 가져오는 AJAX 요청
    $.ajax({
      url: 'http://localhost:8080/accounts/inactive',
      method: 'GET',
      success: function (accounts) {
        var accountContainer = $('.inactiveAccount-card-container');
        accountContainer.empty();

        // 계좌 데이터 렌더링
        accounts.forEach(function (account) {
          const formattedDate = formatDate(account.createDate);
          
          var accountItem = `
      <div class="account-card p-3 shadow-sm disabled" id="account-${account.id}">
        <h5 class="del">${account.name}</h5>
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

    //삭제  
    $('.activeAccount-card-container').on('click', '.delete-btn', function () {
      var accountId = $(this).data('id');

      if (confirm("이 계좌을 삭제하시겠습니까?")) {
        $.ajax({
          url: 'http://localhost:8080/accounts/' + accountId,
          method: 'DELETE',
          success: function (response) {
            console.log('계좌 삭제 성공:', response);
            alert("계좌가 성공적으로 삭제되었습니다!");

            $('#account-' + accountId).remove();
          },
          error: function (xhr, status, error) {
            console.error('계좌 삭제 중 오류 발생:', error);
            if (xhr.status === 403) {
              alert(xhr.responseText);
            } else {
              alert("계좌 삭제 중 오류가 발생했습니다.");
            }
          }
        });
      }
    });


    // 계좌 수정 버튼 클릭 이벤트
    $('.activeAccount-card-container').on('click', '.edit-btn', function () {
      var accountId = $(this).data('id');
      var accountName = $(this).data('name');
      var accountBalance = $(this).data('balance');
      var accountType = $(this).data('type');

      // 수정할 계좌 정보를 폼에 채워넣기
      $('#editAccountId').val(accountId);
      $('#editAccountName').val(accountName);
      $('#editBalance').val(accountBalance);
      $('#editAccountType').val(accountType);

      // 수정 폼 표시
      $('#editAccountModal').modal('show');
    });

    // 계좌 수정 제출
    $('#editAccountForm').on('submit', function (event) {
      event.preventDefault();

      var accountId = $('#editAccountId').val();
      var accountName = $('#editAccountName').val();
      var balance = $('#editBalance').val();
      var accountType = $('#editAccountType').val();

      // // 입력값 검증
      if (!accountName) {
        alert("계좌 이름을 입력하세요.");
        return;
      }

      if (!balance || isNaN(balance) || balance < 0) {
        alert("유효한 금액을 입력하세요.");
        return;
      }

      if (!accountType) {
        alert("계좌 유형을 선택하세요.");
        return;
      }

      // 수정된 데이터 전송
      var accountData = {
        name: accountName,
        balance: balance,
        status: accountType
      };

      // 서버로 PUT 요청
      $.ajax({
        url: 'http://localhost:8080/accounts/' + accountId,
        method: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify(accountData),
        success: function (response) {
          alert("계좌가 성공적으로 수정되었습니다.");
          $('#editAccountModal').modal('hide');
          location.reload(); // 수정 후 페이지 새로고침
        },
        error: function (xhr, status, error) {
          console.error('계좌 수정 중 오류 발생:', error);

          if (xhr.status === 403) {
            alert(xhr.responseText)
          } else {
            alert("계좌 수정 중 오류가 발생했습니다.");
          }
        }
      });
    });
  });
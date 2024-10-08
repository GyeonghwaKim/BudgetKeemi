$(document).ready(function () {

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

    // select에 들어갈 카테고리 목록 (수정모달)
    $.ajax({
      url: 'http://localhost:8080/categories/active',
      method: 'GET',
      success: function (categories) {
        var editCategorySelect = $('#editCategory');
        editCategorySelect.empty();
        categories.forEach(function (category) {
          var option = $('<option></option>').val(category.id).text(category.status + ' - ' + category.name);
          editCategorySelect.append(option);
        });
      },
      error: function (error) {
        console.error("카테고리 데이터를 불러오는 중 오류 발생:", error);
      }
    });

    //예산 post
    $("#submitBudget").click(function () {
      const category = $("#category").val();
      const goalAmount = $("#goalAmount").val();
      const startDate = $("#startDate").val();
      const endDate = $("#endDate").val();


      // 입력값 검증
      if (!category) {
        alert("카테고리를 선택하세요.");
        return;  
      }

      if (!goalAmount || isNaN(goalAmount) || goalAmount <= 0) {
        alert("유효한 금액을 입력하세요.");
        return;  
      }

      if (!startDate) {
        alert("시작 날짜를 선택하세요.");
        return; 
      }

      if (!endDate) {
        alert("종료 날짜를 선택하세요.");
        return; 
      }


      const budgetData = {
        categoryId: category,
        goalAmount: goalAmount,
        startDate: startDate,
        endDate: endDate
      };

      $.ajax({
        url: 'http://localhost:8080/budgets',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(budgetData),
        success: function (response) {
          console.log('Success:', response);
          alert("예산이 성공적으로 저장되었습니다!");
          location.reload();

        },
        error: function (xhr, status, error) {
          console.error('Error:', error);
          if (xhr.status === 403) {
            alert(xhr.responseText);
          }else if(xhr.status===400){
            alert(xhr.responseText)
          }
           else {
            alert("예산 작성 중 오류가 발생했습니다.");
          }
        }
      });
    });
    //예산 내역 get
    $.ajax({
      url: 'http://localhost:8080/budgets',
      method: 'GET',
      success: function (budgets) {
        var budgetContainer = $('.budget-card-container');
        budgetContainer.empty();

        budgets.reverse();

        budgets.forEach(function (budget) {
          var budgetItem = `
               <div class="budget-card p-3 shadow-sm" id="budget-${budget.id}">
          <button class="btn btn-danger btn-sm float-end delete-btn" data-id="${budget.id}">삭제</button>
          <button class="btn btn-primary btn-sm float-end edit-btn" data-id="${budget.id}" data-category="${budget.categoryId}" data-goalAmount="${budget.goalAmount}" data-startDate="${budget.startDate}" data-endDate="${budget.endDate}">수정</button>
          <h5>${budget.categoryName}</h5>
          <h6><span>${budget.startDate}</span> ~ <span>${budget.endDate}</span></h6>
          <span>${budget.goalAmount.toLocaleString()} 원</span>
        </div>
              `;
          // 만들어진 HTML을 budgetContainer에 추가
          budgetContainer.append(budgetItem);
        });
      },
      error: function (error) {
        console.error("예산 데이터를 불러오는 중 오류 발생:", error);
      }
    });



    // 삭제 버튼 클릭 이벤트 핸들러 추가
    $('.budget-card-container').on('click', '.delete-btn', function () {
      var budgetId = $(this).data('id');

      if (confirm("이 예산을 삭제하시겠습니까?")) {
        $.ajax({
          url: 'http://localhost:8080/budgets/' + budgetId,
          method: 'DELETE',
          success: function (response) {
            console.log('예산 삭제 성공:', response);
            alert("예산이 성공적으로 삭제되었습니다!");

            $('#budget-' + budgetId).remove();
          },
          error: function (xhr, status, error) {
            console.error('예산 삭제 중 오류 발생:', error);
            alert("예산 삭제 중 오류가 발생했습니다.");
          }
        });
      }
    });



    // 수정 버튼 이벤트
    $('.budget-card-container').on('click', '.edit-btn', function () {
      var budgetId = $(this).data('id');
      var categoryId = $(this).data('category');
      var goalAmount = $(this).data('goalamount');
      var startDate = $(this).data('startdate');
      var endDate = $(this).data('enddate');

      console.log(budgetId);
      console.log(goalAmount);


      // 폼에 기존 예산 데이터를 채움
      $('#editBudgetId').val(budgetId);
      $('#editGoalAmount').val(goalAmount);
      $('#editStartDate').val(startDate);
      $('#editEndDate').val(endDate);
      $('#editCategory').val(categoryId);

      // 수정 모달 표시
      $('#editBudgetModal').modal('show');
    });

    // 수정 폼 제출
    $('#editBudgetForm').on('submit', function (event) {

      event.preventDefault();
      var budgetId = $('#editBudgetId').val();
      var goalAmount = $('#editGoalAmount').val();
      var startDate = $('#editStartDate').val();
      var endDate = $('#editEndDate').val();
      var category = $('#editCategory').val();

      //입력값 검증
      if (!category) {
        alert("카테고리를 선택하세요.");
        return;  
      }

      if (!goalAmount || isNaN(goalAmount) || goalAmount <= 0) {
        alert("유효한 금액을 입력하세요.");
        return;  
      }

      if (!startDate) {
        alert("시작 날짜를 선택하세요.");
        return; 
      }

      if (!endDate) {
        alert("종료 날짜를 선택하세요.");
        return; 
      }
      
      // 수정된 데이터 전송
      var updatedBudgetData = {
        goalAmount: goalAmount,
        startDate: startDate,
        endDate: endDate,
        category: category
      };

      $.ajax({
        url: 'http://localhost:8080/budgets/' + budgetId,
        method: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify(updatedBudgetData),
        success: function (response) {
          alert("예산이 성공적으로 수정되었습니다!");
          $('#editBudgetModal').modal('hide');
          location.reload();// 수정 후 목록 새로고침
        },
        error: function (xhr, status, error) {
          console.error('예산 수정 중 오류 발생:', error);
          alert("예산 수정 중 오류가 발생했습니다.");
        }
      });
    });

  });


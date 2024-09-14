$(document).ready(function () {

    $('#submitCategory').on('click', function () {

      var categoryName = $('#categoryName').val();
      var categoryType = $('#categoryType').val();

      //입력값 검증
      if (!categoryName) {
        alert("카테고리 이름을 작성하세요");
        return;
      }

      if (!categoryType) {
        alert("카테고리 유형을 선택하세요.");
        return;
      }


      var data = {
        name: categoryName,
        status: categoryType
      };
      $.ajax({
        url: '/categories', // POST 요청을 보낼 엔드포인트
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(data),
        success: function (response) {
          alert('카테고리가 성공적으로 추가되었습니다.');
          location.reload();
        },
        error: function (error) {
          console.error('카테고리를 저장하는 중 오류 발생:', error);
          alert('카테고리를 저장하는 중 오류가 발생했습니다.');
        }
      });

    });


    $.ajax({
      url: '/categories/categoryStatus',
      method: 'GET',
      dataType: 'json',
      success: function (categories) {
        var categoryType = $('#categoryType');
        categoryType.empty(); // 기존 옵션 제거

        // 서버에서 가져온 카테고리 목록을 추가
        categories.forEach(function (category) {
          const option = `<option value="${category.category}">${category.description}</option>`;
          categoryType.append(option);
        });
      },
      error: function (error) {
        console.error('카테고리 정보를 가져오는 중 오류 발생:', error);
      }
    });


    //카테고리 목록
    $.ajax({
      url: 'http://localhost:8080/categories/active',
      method: 'GET',
      success: function (categories) {
        var categoryContainer = $('.category-card-container');
        categoryContainer.empty();

        categories.forEach(function (category) {

          var categoryItem = `
              <div class="category-card p-3 shadow-sm" id="category-${category.id}">
                <button class="btn btn-primary btn-sm float-end edit-btn" data-id="${category.id}" data-name="${category.name}" data-status="${category.status}">수정</button>
                <button class="btn btn-danger btn-sm float-end delete-btn" data-id="${category.id}">삭제</button>
                <h5>${category.name}</h5>
                <h6>${category.status}</h6>
              </div>
            `;
          categoryContainer.append(categoryItem);
        });
      },
      error: function (error) {
        console.error("계좌 데이터를 불러오는 중 오류 발생:", error);
      }
    });
    //카테고리 삭제
    $('.category-card-container').on('click', '.delete-btn', function () {
      var categoryId = $(this).data('id');

      if (confirm("이 카테고리를을 삭제하시겠습니까?")) {
        $.ajax({
          url: 'http://localhost:8080/categories/' + categoryId,
          method: 'DELETE',
          success: function (response) {
            console.log('카테고리 삭제 성공:', response);
            alert("카테고리가 성공적으로 삭제되었습니다!");

            $('#category-' + categoryId).remove();
          },
          error: function (xhr, status, error) {
            console.error('카테고리 삭제 중 오류 발생:', error);

            if (xhr.status === 403) {
              alert(xhr.responseText);
            } else {
              alert("카테고리 삭제 중 오류가 발생했습니다.");
            }
          }
        });
      }
    });


    // 수정 버튼 클릭 이벤트
    $('.category-card-container').on('click', '.edit-btn', function () {
      var categoryId = $(this).data('id');
      var categoryName = $(this).data('name');
      var categoryStatus = $(this).data('status');

      // 수정할 카테고리 정보를 폼에 채워넣기
      $('#editCategoryId').val(categoryId);
      $('#editCategoryName').val(categoryName);
      $('#editCategoryStatus').val(categoryStatus);

      // 수정 폼 표시
      $('#editCategoryModal').modal('show');
    });

    // 카테고리 수정 폼 제출
    $('#editCategoryForm').on('submit', function (event) {
      event.preventDefault();

      var categoryId = $('#editCategoryId').val();
      var updatedName = $('#editCategoryName').val();
      var updatedStatus = $('#editCategoryStatus').val();

      //입력값 검증
      if (!categoryName) {
        alert("카테고리 이름을 작성하세요");
        return;
      }

      if (!categoryType) {
        alert("카테고리 유형을 선택하세요.");
        return;
      }


      $.ajax({
        url: 'http://localhost:8080/categories/' + categoryId,
        method: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify({
          name: updatedName,
          status: updatedStatus
        }),
        success: function (response) {
          alert('카테고리가 성공적으로 수정되었습니다.');
          $('#editCategoryModal').modal('hide');
          location.reload(); // 수정 후 카테고리 목록 다시 로드
        },
        error: function (xhr, status, error) {
          console.error('카테고리 수정 중 오류 발생:', error);

          if (xhr.status === 403) {
            alert(xhr.responseText);

          } else {
            alert('카테고리 수정 중 오류가 발생했습니다.');

          }
        }
      });
    });

  });
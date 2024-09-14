$(document).ready(function() {
    // 프로필 정보 불러오기
    $.ajax({
      url: 'http://localhost:8080/members/profile',
      method: 'GET',
      success: function(respMember) {
        //$('#username').text(respMember.username);
        $('#email').text(respMember.email);
        $('#joinDate').text(new Date(respMember.joinDate).toLocaleDateString());
        $('#profileImage').attr('src', respMember.profileImge);
      },
      error: function(error) {
        console.error("프로필 데이터를 불러오는 중 오류 발생:", error);
        alert("프로필 데이터를 불러오는 중 오류가 발생했습니다.");
      }
    });

    // 프로필 이미지 저장
    $('#submitProfileImg').click(function() {

      var formData = new FormData();
      var profileImg = $('#profileImg')[0].files[0];
      formData.append('profileImg', profileImg);

      //입력값 검증
      if(!profileImg){
        alert("파일을 입력하세요")
        return;
      }

      $.ajax({
        url: 'http://localhost:8080/members/profileImg',
        method: 'PUT',
        data: formData,
        processData: false,
        contentType: false,
        success: function(respMember) {
          alert("프로필 이미지가 성공적으로 업데이트되었습니다.");
          $('#profileImage').attr('src', respMember.profileImge);
        },
        error: function(xhr, status, error) {
          console.error('프로필 이미지 저장 중 오류 발생:', error);

          if(xhr.status===400){
            alert(xhr.responseText);
          }
          alert("프로필 이미지 저장 중 오류가 발생했습니다.");
        }
      });
    });
  });
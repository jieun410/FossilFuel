var mapContainer = document.getElementById('map'), // 지도를 표시할 div
    mapOption = {
        center: new kakao.maps.LatLng(35.91124391258411, 128.80795633875374), // 지도의 중심좌표
        level: 5 // 지도의 확대 레벨
    };

var map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다

// 지도에 클릭 이벤트를 등록합니다
kakao.maps.event.addListener(map, 'click', function (mouseEvent) {
    var latlng = mouseEvent.latLng;

    var latitude = latlng.getLat();
    var longitude = latlng.getLng();

    // 클릭한 위치 정보를 서버로 전송합니다
    sendLocationToServer(latitude, longitude);
});

function sendLocationToServer(lat, lng) {
    fetch('/location', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            latitude: lat,
            longitude: lng
        })
    })
        .then(response => {
            if (response.ok) {
                console.log('데이터 전송 성공');
            } else {
                console.error('데이터 전송 실패');
            }
        })
        .catch(error => console.error('에러 발생:', error));
}
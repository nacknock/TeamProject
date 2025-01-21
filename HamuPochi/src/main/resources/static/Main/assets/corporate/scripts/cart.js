const minus_btn = document.querySelectorAll('.btn-minus');//+버튼
const plus_btn = document.querySelectorAll('.btn-plus');//-버튼
const del_btn = document.querySelectorAll('.delbtn');//x버튼

//초기 total_fin값 설정
const total_price = document.querySelectorAll('.total_one_price');//우측 합계하기 이전 각 물건의 총가격
let total_fin = 0;
total_price.forEach(total_one_price => {
    total_fin += parseInt(total_one_price.textContent,10);
});
document.getElementById('total_fin').textContent = total_fin;


//다수 항목에 적용
//초기 항목에 이벤트 리스너를 적용합니다.
minus_btn.forEach(button => {
    button.addEventListener('click', () => {
        minusButtonEventListener(button);
    });
});
plus_btn.forEach(button => {
    button.addEventListener('click', () => {
        plusButtonEventListener(button);
    });
});
del_btn.forEach(button => {
    button.addEventListener('click', () => {
        delButtonEventListener(button);
    });
});

//수량감소 이벤트
function minusButtonEventListener(button){
    // id의 '-' 뒤 넘버만 남기기
    const parts = button.id.split('-');
    const number = parts[1];
    //caidx 구하기
    const caidx = document.getElementById('hidden-'+number).value;
    //숫자 변경할 요소들 불러오기
    let amount = parseInt(document.getElementById('amount-'+number).value,10);//각 물건의 수량
    if(amount < 1){
        alert("1개보다 적게 설정할 수 없습니다.");
        document.getElementById('amount-'+number).value = 1;
        amount = 1;
    }
    const p_price = parseInt(document.getElementById('p_price-'+number).textContent,10);//테이블에서 각 물건의 하나당 가격
    let amount_total = parseInt(document.getElementById('amount_total-'+number).textContent,10);//테이블에서 각 물건의 total칸
    let total_one_price = parseInt(document.getElementById('total_one_price-'+number).textContent,10);//우측 합계하기 이전 각 물건의 총가격
    let total_fin = parseInt(document.getElementById('total_fin').textContent,10);//모든 물건의 총합산
    //계산
    amount_total = p_price*amount;
    total_fin -= (total_one_price - p_price*amount);
    total_one_price = p_price*amount;
    //변경된 값 DB 저장
    changeAmount(amount,caidx);
    //값 저장
    document.getElementById('amount_total-'+number).textContent = amount_total;
    document.getElementById('total_one_price-'+number).textContent = total_one_price;
    document.getElementById('total_fin').textContent = total_fin;

    
};

//수량증가 이벤트
function plusButtonEventListener(button){
    // id의 '-' 뒤 넘버만 남기기
    const parts = button.id.split('-');
    const number = parts[1];
    //caidx 구하기
    const caidx = document.getElementById('hidden-'+number).value;
    //숫자 변경할 요소들 불러오기
    let amount = parseInt(document.getElementById('amount-'+number).value,10);//각 물건의 수량
    if(amount > 99){
        alert("100개 이상으로는 설정할 수 없습니다.");
        document.getElementById('amount-'+number).value = 99;
        amount = 99;
    }
    const p_price = parseInt(document.getElementById('p_price-'+number).textContent,10);//테이블에서 각 물건의 하나당 가격
    let amount_total = parseInt(document.getElementById('amount_total-'+number).textContent,10);//테이블에서 각 물건의 total칸
    let total_one_price = parseInt(document.getElementById('total_one_price-'+number).textContent,10);//우측 합계하기 이전 각 물건의 총가격
    let total_fin = parseInt(document.getElementById('total_fin').textContent,10);//모든 물건의 총합산
    //계산
    amount_total = p_price*amount;
    total_fin += (p_price*amount - total_one_price);
    total_one_price = p_price*amount;
    //변경된 값 DB 저장
    changeAmount(amount,caidx);
    //값 저장
    document.getElementById('amount_total-'+number).textContent = amount_total;
    document.getElementById('total_one_price-'+number).textContent = total_one_price;
    document.getElementById('total_fin').textContent = total_fin;
};
//유저가 물건 개수를 직접 수정하는 경우
const amount_input = document.querySelectorAll('.amount-input');

amount_input.forEach(amount_input => {
    amount_input.addEventListener('input', (event) => {
        // id의 '-' 뒤 넘버만 남기기
        const parts = event.target.id.split('-');
        const number = parts[1];
        let amount = event.target.value; // 현재 입력된 값
        if(!amount){
            return false;
        }else{
            if(amount > 99){
                alert("100개 이상으로는 설정할 수 없습니다.");
                document.getElementById('amount-'+number).value = 99;
                amount = 99;
            }else if(amount < 1){
                alert("1개보다 적게 설정할 수 없습니다.");
                document.getElementById('amount-'+number).value = 1;
                amount = 1;
            }
        }
        //caidx 구하기
        const caidx = document.getElementById('hidden-'+number).value;
        //숫자 변경할 요소들 불러오기
        const p_price = parseInt(document.getElementById('p_price-'+number).textContent,10);//테이블에서 각 물건의 하나당 가격
        let amount_total = parseInt(document.getElementById('amount_total-'+number).textContent,10);//테이블에서 각 물건의 total칸
        let total_one_price = parseInt(document.getElementById('total_one_price-'+number).textContent,10);//우측 합계하기 이전 각 물건의 총가격
        let total_fin = parseInt(document.getElementById('total_fin').textContent,10);//모든 물건의 총합산
        //변경된 개수에 맞춰 다시 계산
        total_fin -= amount_total;
        total_fin += p_price*amount;
        amount_total = p_price*amount;
        total_one_price = p_price*amount;
        //변경된 값 DB 저장
        changeAmount(amount,caidx);
        //값 저장
        document.getElementById('amount_total-'+number).textContent = amount_total;
        document.getElementById('total_one_price-'+number).textContent = total_one_price;
        document.getElementById('total_fin').textContent = total_fin;

    });
});

//삭제 이벤트
function delButtonEventListener(button){
    // id의 '-' 뒤 넘버만 남기기
    const parts = button.id.split('-');
    const number = parts[1];
    //숫자 변경할 요소들 불러오기
    let amount_total = parseInt(document.getElementById('amount_total-'+number).textContent,10);//테이블에서 각 물건의 total칸
    let total_fin = parseInt(document.getElementById('total_fin').textContent,10);//모든 물건의 총합산
    //변경된 개수에 맞춰 다시 계산
    total_fin -= amount_total;
    //값 저장
    document.getElementById('total_fin').textContent = total_fin;
    //caidx 구하기
    const caidx = document.getElementById('hidden-'+number).value;
    //장바구니 내 tr 삭제 및 cart table에서 삭제 ajax
    const xhr = new XMLHttpRequest();
    xhr.open('POST', '/cart_delete', true);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.send(`caidx=${caidx}`);

    xhr.onreadystatechange = function () {

        if (xhr.readyState == XMLHttpRequest.DONE && xhr.status == 200) {

            if (xhr.response != null) {
                //navbar cart 개수 최신화(-1)
                const cart_icon = document.getElementById("cart-cnt");
                const cart_icon_mini = document.getElementById("cart-cnt-mini");
                
                const response = JSON.parse(xhr.responseText);

                if(response.caidx === 1){
                    alert("장바구니에서 상품이 제거되었습니다.");
                    const cart_num = parseInt(document.getElementById("cart-cnt").textContent,10);

                    cart_icon.textContent = cart_num - 1;
                    cart_icon_mini.textContent = cart_num - 1;

                    const row = button.closest('tr'); // 현재 버튼의 가장 가까운 <tr> 요소 찾기
                    row.remove(); // <tr> 삭제
                    document.getElementById('total_one-'+number).remove();//우측 합계하기 이전 각 물건의 총가격

                    checkTable();//상품 0개일때 메시지 띄우기

                }else if(response.caidx == -1){
                    alert("상품제거에 실패했습니다!-3");
                }

                


            } else if(xhr.response == null){

                alert('실패하였습니다!관리자에게 문의해주세요.');
                console.log('fail');

            }
        }
        else if (xhr.status == 500){

            alert('실패2!');
            
        }
    
    };

}

// 장바구니에 상품 없을때
function checkTable() {
    const tbody = document.getElementById('tbody');
    const noneProduct = document.getElementById('none-product');

    // tbody 내의 td 요소 수 확인
    const tdCount = tbody.getElementsByTagName('td').length;

    // td가 없으면 emptyMessage를 보이게 하고, 있으면 숨김
    if (tdCount === 0) {
        noneProduct.style.display = 'block';
    } else {
        noneProduct.style.display = 'none';
    }
}

// 페이지가 로드될 때 테이블 상태 확인
window.onload = checkTable;

// 수량을 +-시켰을때 ajax 처리
function changeAmount(amount,caidx){
    const xhr = new XMLHttpRequest();
    xhr.open('POST', '/amount_update', true);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.send(`amount=${amount}&caidx=${caidx}`);

    xhr.onreadystatechange = function () {

        if (xhr.readyState == XMLHttpRequest.DONE && xhr.status == 200) {

            if (xhr.response != null) {
                //navbar cart amount 최신화
                
                const response = JSON.parse(xhr.responseText);

                if(response.caidx === 1){

                }else if(response.caidx === -1){
                    alert("수량변경에 실패했습니다!-3");
                }

            } else if(xhr.response == null){

                alert('실패하였습니다!관리자에게 문의해주세요.');
                console.log('fail');

            }
        }
        else if (xhr.status == 500){

            alert('실패2!');
            
        }
    
    };
}

function GotoCheck(){
    location.href = "/checkout";
}
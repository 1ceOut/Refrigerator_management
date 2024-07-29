import React, { useEffect, useState } from 'react';
import axios from 'axios';

const Foodlist = () => {
    const [savedBarcodes, setSavedBarcodes] = useState([]);

    const fetchSavedBarcodes = async () => {
        try {
            const response = await axios.get('http://localhost:8080/api/list');
            setSavedBarcodes(response.data);
        } catch (error) {
            console.error('Error fetching saved barcodes', error);
        }
    };

    useEffect(() => {
        fetchSavedBarcodes();
    }, []);

    const deleteBarcode = async (barcode) => {
        try {
            const response = await axios.delete(`http://localhost:8080/api/barcodes/${barcode}`);


                fetchSavedBarcodes();
                alert('상품이 삭제되었습니다.');

        } catch (error) {
            console.error('Error deleting barcode', error);
        }
    };

    return (
        <div>
            <h2>등록된 상품 목록</h2>
            {savedBarcodes.length === 0 ? (
                <p>등록된 상품이 없습니다.</p>
            ) : (
                savedBarcodes.map((barcode, index) => (
                    <div key={index} style={{ marginBottom: '20px' }}>
                        <p><strong>제품명:</strong> {barcode.productName}</p>
                        <p><strong>바코드:</strong> {barcode.barcode}</p>
                        <p><strong>유통기한:</strong> {barcode.expiryDate}</p>
                        <p><strong>회사명:</strong> {barcode.companyName}</p>
                        <p><strong>주소:</strong> {barcode.address}</p>
                        <p><strong>식품 유형:</strong> {barcode.productType}</p>
                        <p><strong>허가 날짜:</strong> {barcode.permissionDate}</p>
                        <button
                            style={{ cursor: 'pointer', color: 'gray' }}
                            onClick={() => {
                                const confirmDelete = window.confirm("해당 식품 삭제할꺼?");
                                if (confirmDelete) {
                                    deleteBarcode(barcode.barcode);
                                }
                            }}
                        >
                            삭제
                        </button>
                        <hr />
                    </div>
                ))
            )}
        </div>
    );
};

export default Foodlist;

import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';

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

    const deleteBarcode = async (productName) => {
        try {
            await axios.delete(`/api/barcodes/${productName}`);
            fetchSavedBarcodes();
            alert('상품 삭제함');
        } catch (error) {
            console.error('Error deleting barcode', error);
        }
    };

    return (
        <div>
            <h2>등록된 상품 목록</h2>
            {savedBarcodes.length === 0 ? (
                <p>등록된 상품 없음</p>
            ) : (
                savedBarcodes.map((barcode, index) => (
                    <div key={index} style={{ marginBottom: '20px' }}>
                        <p><strong>제품명 : </strong> {barcode.productName}</p>
                        <p><strong>바코드 : </strong> {barcode.barcode}</p>
                        <p><strong>수량 : </strong> {barcode.count}</p>
                        <p><strong>등록 일자 : </strong>{barcode.createdDate}</p>
                        <p><strong>유통 기한 : </strong> {barcode.expiryDate}</p>
                        <p><strong>종 류 : </strong> {barcode.lcategory}</p>
                        <p><strong>상세종류 : </strong> {barcode.scategory}</p>
                        <button
                            style={{ cursor: 'pointer', color: 'gray' }}
                            onClick={() => {
                                const confirmDelete = window.confirm("해당 식품 삭제할꺼?");
                                if (confirmDelete) {
                                    deleteBarcode(barcode.productName);
                                }
                            }}
                        >
                            삭제
                        </button>
                        <Link to={`/detailfood/${barcode.productName}/${barcode.scategory}/${barcode.productType}`}>
                            <button
                                style={{ cursor: 'pointer', color: 'gray' }}
                            >
                                영양정보 확인하기
                            </button>
                        </Link>
                        <hr />
                    </div>
                ))
            )}
        </div>
    );
};

export default Foodlist;

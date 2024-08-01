import React, { useEffect, useState } from 'react';
import axios from "axios";
import { useNavigate } from "react-router-dom";

const UserDataInput = () => {
    const [barcode, setBarcode] = useState('');
    const [productName, setProductName] = useState('');
    const [expiryDate, setExpiryDate] = useState('');
    const [count, setCount] = useState('');
    const [productType, setProductType] = useState('');
    const [lcategory, setLcategory] = useState('');
    const [scategory, setScategory] = useState('');
    const [lcategories, setLcategories] = useState([]);
    const [scategories, setScategories] = useState([]);
    const [showLcategories, setShowLcategories] = useState(false);
    const [showScategories, setShowScategories] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        if (productType === '가공식품') {
            fetchRecipeLcategories();
        } else if (productType === '원자재성 식품') {
            fetchLcategories();
        }
    }, [productType]);

    const fetchLcategories = async () => {
        try {
            const response = await axios.post('/food/_search', {
                query: { match_all: {} },
                size: 4000
            });

            const hits = response.data.hits.hits;
            const uniqueLcategories = [...new Set(hits.map(hit => hit._source.lcategory))];
            setLcategories(uniqueLcategories);
        } catch (error) {
            console.error('Error fetching lcategories', error);
        }
    };

    const fetchScategories = async (selectedLcategory) => {
        try {
            const response = await axios.post('/food/_search', {
                query: {
                    bool: { must: [{ match: { lcategory: selectedLcategory } }] }
                },
                size: 4000
            });

            const hits = response.data.hits.hits;
            const uniqueScategories = [...new Set(hits.map(hit => hit._source.scategory))];
            setScategories(uniqueScategories);
        } catch (error) {
            console.error('Error fetching scategories', error);
        }
    };

    const fetchRecipeLcategories = async () => {
        try {
            const response = await axios.post('/recipe/_search', {
                query: { match_all: {} },
                size: 7000
            });

            const hits = response.data.hits.hits;
            const uniqueLcategories = [...new Set(hits.map(hit => hit._source.lcategory))];
            setLcategories(uniqueLcategories);
        } catch (error) {
            console.error('Error fetching recipe lcategories', error);
        }
    };

    const handleProductTypeChange = (e) => {
        const value = e.target.value;
        setProductType(value);
        setShowLcategories(true);
        setShowScategories(false);
    };

    const handleLcategoryChange = (e) => {
        const value = e.target.value;
        setLcategory(value);

        fetchScategories(value);
        setShowScategories(true);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const product = { barcode, productName, expiryDate, count, productType, lcategory, scategory };

        try {
            const response = await axios.post('http://localhost:8080/api/barcodes', product);
            console.log('Product saved successfully:', response.data);
            navigate('/barlist');
        } catch (error) {
            console.error('Error saving product to DB', error);
        }
    };

    return (
        <div>
            <h2>상품 정보 직접 입력</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>바코드:</label>
                    <input type="text" value={barcode} onChange={(e) => setBarcode(e.target.value)} placeholder="바코드정보가 없다면 입력하지마셈"/>
                </div>
                <div>
                    <label>제품명:</label>
                    <input type="text" value={productName} onChange={(e) => setProductName(e.target.value)} required/>
                </div>
                <div>
                    <label>유통기한:</label>
                    <input type="date" value={expiryDate} onChange={(e) => setExpiryDate(e.target.value)} required/>
                </div>
                <div>
                    <label>수량 :</label>
                    <input type="text" value={count} onChange={(e) => setCount(e.target.value)} required/>
                </div>
                <div>
                    <label>식품 유형:</label>
                    <select onChange={handleProductTypeChange} value={productType}>
                        <option value="" disabled>========</option>
                        <option value="가공식품">가공식품</option>
                        <option value="원자재성 식품">원자재성 식품</option>
                    </select>
                </div>
                {showLcategories && (
                    <div>
                        <label>종 류:</label>
                        <select name="lcategory" value={lcategory} onChange={handleLcategoryChange}>
                            {lcategories.map((category, index) => (
                                <option key={index} value={category}>{category}</option>
                            ))}
                        </select>
                    </div>
                )}
                {showScategories && (
                    <div>
                        <label>세부 종류:</label>
                        <select name="scategory" value={scategory} onChange={(e) => setScategory(e.target.value)}>
                            {scategories.map((category, index) => (
                                <option key={index} value={category}>{category}</option>
                            ))}
                        </select>
                    </div>
                )}
                <button type="submit">상품 등록</button>
            </form>
        </div>
    );
};

export default UserDataInput;

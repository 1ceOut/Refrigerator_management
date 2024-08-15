import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const PRODUCT_TYPES = {
    PROCESSED: '가공식품',
    RAW: '원자재성 식품'
};

const UserDataInput = () => {
    const [form, setForm] = useState({
        barcode: '',
        productName: '',
        expiryDate: '',
        count: '',
        productType: '',
        lcategory: '',
        scategory: ''
    });

    const [categories, setCategories] = useState({
        lcategories: [],
        scategories: []
    });

    const [showCategories, setShowCategories] = useState({
        showLcategories: false,
        showScategories: false
    });

    const navigate = useNavigate();

    useEffect(() => {
        if (form.productType === PRODUCT_TYPES.PROCESSED) {
            fetchRecipeLcategories();
        } else if (form.productType === PRODUCT_TYPES.RAW) {
            fetchFoodLcategories();
        }
    }, [form.productType]);

    const fetchFoodLcategories = async () => {
        try {
            const response = await axios.post('/food/_search', {
                size: 0,
                aggs: {
                    unique_lcategories: {
                        terms: {
                            field: "lcategory.keyword",
                            size: 1000
                        }
                    }
                }
            });

            const buckets = response.data.aggregations.unique_lcategories.buckets;
            const uniqueLcategories = buckets.map(bucket => bucket.key);
            setCategories(prevState => ({ ...prevState, lcategories: uniqueLcategories }));
        } catch (error) {
            console.error('Error fetching food lcategories', error);
        }
    };





    const fetchFoodScategories = async (selectedLcategory) => {
        try {
            const response = await axios.get('/food/_search', { params: { q: `lcategory:"${selectedLcategory}"`, size: 4000 } });
            const hits = response.data.hits.hits;
            const uniqueScategories = [...new Set(hits.map(hit => hit._source.scategory))];
            console.log('푸드에서 고른 것:', uniqueScategories);
            setCategories(prevState => ({ ...prevState, scategories: uniqueScategories }));
        } catch (error) {
            console.error('Error fetching food scategories', error);
        }
    };

    const fetchRecipeLcategories = async () => {
        try {
            const response = await axios.post('/recipe/_search', {
                size: 0,
                aggs: {
                    unique_lcategories: {
                        terms: {
                            script: {
                                source: "doc['lcategory'].value",
                                lang: "painless"
                            },
                            size: 100
                        }
                    }
                }
            });

            const buckets = response.data.aggregations.unique_lcategories.buckets;
            const uniqueLcategories = buckets.map(bucket => bucket.key);
            setCategories(prevState => ({ ...prevState, lcategories: uniqueLcategories }));
        } catch (error) {
            console.error('Error fetching recipe lcategories', error);
        }
    };

    const fetchRecipeScategories = async (selectedLcategory) => {
        try {
            const response = await axios.get('/recipe/_search', { params: { q: `lcategory:"${selectedLcategory}"`, size: 1000 } });
            const hits = response.data.hits.hits;
            const uniqueScategories = [...new Set(hits.map(hit => hit._source.scategory))];
            console.log('레시피에서 고른 것:', uniqueScategories);
            setCategories(prevState => ({ ...prevState, scategories: uniqueScategories }));
        } catch (error) {
            console.error('Error fetching recipe scategories', error);
        }
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setForm(prevState => ({ ...prevState, [name]: value }));
    };

    const handleProductTypeChange = (e) => {
        const value = e.target.value;
        setForm(prevState => ({
            ...prevState,
            productType: value,
            lcategory: '',
            scategory: ''
        }));

        setCategories({
            lcategories: [],
            scategories: []
        });

        setShowCategories({
            showLcategories: true,
            showScategories: false
        });
    };

    const handleLcategoryChange = (e) => {
        const value = e.target.value;
        setForm(prevState => ({ ...prevState, lcategory: value }));

        if (form.productType === PRODUCT_TYPES.PROCESSED) {
            fetchRecipeScategories(value);
        } else if (form.productType === PRODUCT_TYPES.RAW) {
            fetchFoodScategories(value);
        }

        setShowCategories(prevState => ({ ...prevState, showScategories: true }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const product = { ...form };

        try {
            const response = await axios.post('/api/barcodes', product);
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
                    <input
                        type="text"
                        name="barcode"
                        value={form.barcode}
                        onChange={handleInputChange}
                        placeholder="바코드정보가 없다면 입력하지마셈"
                    />
                </div>
                <div>
                    <label>제품명:</label>
                    <input
                        type="text"
                        name="productName"
                        value={form.productName}
                        onChange={handleInputChange}
                        required
                    />
                </div>
                <div>
                    <label>유통기한:</label>
                    <input
                        type="date"
                        name="expiryDate"
                        value={form.expiryDate}
                        onChange={handleInputChange}
                        required
                    />
                </div>
                <div>
                    <label>수량 :</label>
                    <input
                        type="text"
                        name="count"
                        value={form.count}
                        onChange={handleInputChange}
                        required
                    />
                </div>
                <div>
                    <label>식품 유형:</label>
                    <select name="productType" onChange={handleProductTypeChange} value={form.productType}>
                        <option value="" disabled>========</option>
                        <option value={PRODUCT_TYPES.PROCESSED}>가공식품</option>
                        <option value={PRODUCT_TYPES.RAW}>원자재성 식품</option>
                    </select>
                </div>
                {form.productType === PRODUCT_TYPES.PROCESSED && showCategories.showLcategories && (
                    <div>
                        <label>가공식품 종류:</label>
                        <select name="lcategory" value={form.lcategory} onChange={handleLcategoryChange}>
                            <option value="" disabled>선택하세요</option>
                            {categories.lcategories.map((category, index) => (
                                <option key={index} value={category}>{category}</option>
                            ))}
                        </select>
                    </div>
                )}
                {form.productType === PRODUCT_TYPES.RAW && showCategories.showLcategories && (
                    <div>
                        <label>원자재성 식품 종류:</label>
                        <select name="lcategory" value={form.lcategory} onChange={handleLcategoryChange}>
                            <option value="" disabled>선택하세요</option>
                            {categories.lcategories.map((category, index) => (
                                <option key={index} value={category}>{category}</option>
                            ))}
                        </select>
                    </div>
                )}
                {form.productType === PRODUCT_TYPES.PROCESSED && showCategories.showScategories && categories.scategories.length > 0 && (
                    <div>
                        <label>가공식품 세부 종류:</label>
                        <select name="scategory" value={form.scategory} onChange={handleInputChange}>
                            <option value="" disabled>선택하세요</option>
                            {categories.scategories.map((category, index) => (
                                <option key={index} value={category}>{category}</option>
                            ))}
                        </select>
                    </div>
                )}
                {form.productType === PRODUCT_TYPES.RAW && showCategories.showScategories && categories.scategories.length > 0 && (
                    <div>
                        <label>원자재성 식품 세부 종류:</label>
                        <select name="scategory" value={form.scategory} onChange={handleInputChange}>
                            <option value="" disabled>선택하세요</option>
                            {categories.scategories.map((category, index) => (
                                <option key={index} value={category}>{category}</option>
                            ))}
                        </select>
                    </div>
                )}
                {showCategories.showScategories && categories.scategories.length === 0 && (
                    <div>세부 종류가 없습니다.</div>
                )}
                <button type="submit">상품 등록</button>
            </form>
        </div>
    );
};

export default UserDataInput;

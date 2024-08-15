import React, {useEffect, useRef, useState} from 'react';
import axios from 'axios';
import {Link} from 'react-router-dom';

const PRODUCT_TYPES = {
    PROCESSED: '가공식품',
    RAW: '원자재성 식품'
};

const Barcode = () => {
    const videoRef = useRef(null);
    const canvasRef = useRef(null);

    const [capturedImage, setCapturedImage] = useState(null);
    const [ocrResult, setOcrResult] = useState('');
    const [inferTexts, setInferTexts] = useState([]);
    const [concatenatedText, setConcatenatedText] = useState('');
    const [foodSafetyInfo, setFoodSafetyInfo] = useState(null);
    const [additionalInfo, setAdditionalInfo] = useState({
        productName: '',
        expiryDate: '',
        companyName: '',
        address: '',
        productType: '',
        permissionDate: '',
        count: '',
        lcategory: '',
        scategory: ''
    });
    const [categories, setCategories] = useState({
        lcategories: [],
        scategories: []
    });

    const [showLcategories, setShowLcategories] = useState(false);
    const [showScategories, setShowScategories] = useState(false);

    useEffect(() => {
        const initCamera = async () => {
            const stream = await navigator.mediaDevices.getUserMedia({video: true});
            if (stream && videoRef.current) {
                videoRef.current.srcObject = stream;
            }
        };
        initCamera();
    }, []);

    useEffect(() => {
        if (concatenatedText) {
            fetchFoodSafetyInfo(concatenatedText);
        }
    }, [concatenatedText]);

    useEffect(() => {
        if (additionalInfo.productType === PRODUCT_TYPES.PROCESSED) {
            fetchRecipeLcategories();
        } else if (additionalInfo.productType === PRODUCT_TYPES.RAW) {
            fetchFoodLcategories();
        }
    }, [additionalInfo.productType]);

    const captureImage = () => {
        const video = videoRef.current;
        const canvas = canvasRef.current;
        const context = canvas.getContext('2d');
        if (video && context) {
            canvas.width = video.videoWidth;
            canvas.height = video.videoHeight;
            context.drawImage(video, 0, 0, canvas.width, canvas.height);
            const imageDataUrl = canvas.toDataURL('image/png');
            setCapturedImage(imageDataUrl);
            return imageDataUrl;
        }
        return null;
    };

    const recognizeText = async () => {
        const image = captureImage();
        if (!image) return;
        const apiUrl = '/ocr/custom/v1/32907/69a8f4b90ed9801300e661448e839445d3f21bc6dacddc5bceac7b1e9f1ac92b/general';
        const secretKey = 'R3JMT0JmY05sTGtaWkVvdHVsaHNzSHhWS1VzTVJqd0Y=';
        const response = await axios.post(
            apiUrl,
            {
                images: [
                    {
                        format: 'png',
                        name: 'image',
                        data: image.split(',')[1],
                    },
                ],
                requestId: 'string',
                version: 'V2',
                timestamp: Date.now(),
            },
            {
                headers: {
                    'Content-Type': 'application/json',
                    'X-OCR-SECRET': secretKey,
                },
            }
        );
        if (response.data.images.length === 0 || response.data.images[0].fields.length === 0) {
            alert('텍스트를 인식하지 못했습니다. 다시 시도해 주세요.');
            return;
        }
        setOcrResult(response.data);
        extractInferTexts(response.data);

    };

    const extractInferTexts = (ocrData) => {
        const texts = [];

        ocrData.images.forEach((image) => {
            let concatenatedText = '';

            image.fields.forEach((field) => {
                const filteredText = field.inferText.replace(/\D/g, '');
                concatenatedText += filteredText;
            });

            if (concatenatedText.length === 12 || concatenatedText.length === 13) {
                texts.push(concatenatedText);
            }
        });

        setInferTexts(texts);

        const finalConcatenatedText = texts.join(' ');
        setConcatenatedText(finalConcatenatedText);
    };

    const fetchFoodSafetyInfo = async (barcode) => {
        const apiKey = '3369ab4c48b84f35a46c';
        const apiUrl = `https://openapi.foodsafetykorea.go.kr/api/${apiKey}/C005/json/1/5/BAR_CD=${barcode}`;


        const response = await axios.get(apiUrl);
        setFoodSafetyInfo(response.data);
        if (response.data.C005.total_count !== '0') {
            const product = response.data.C005.row[0];
            setAdditionalInfo({
                productName: product.PRDLST_NM,
                expiryDate: product.POG_DAYCNT,
                companyName: product.BSSH_NM,
                address: product.SITE_ADDR,
                productType: product.PRDLST_DCNM,
                permissionDate: product.PRMS_DT,
                count: '',
                lcategory: '' // 추가된 상태 초기화
            });
        }
    }


    const handleInputChange = (e) => {
        const {name, value} = e.target;
        setAdditionalInfo((prevInfo) => ({
            ...prevInfo,
            [name]: value
        }));
    };

    const saveBarcodeToDB = async () => {
        const product = {
            barcode: concatenatedText,
            productName: additionalInfo.productName,
            expiryDate: additionalInfo.expiryDate,
            count: additionalInfo.count,
            address: additionalInfo.address,
            productType: additionalInfo.productType,
            permissionDate: additionalInfo.permissionDate,
            lcategory: additionalInfo.lcategory,
            scategory: additionalInfo.scategory // scategory 추가
        };


        const response = await axios.post('/api/barcodes', product);
        console.log('Barcode saved successfully:', response.data);
        alert("저장이 되었습니다.");
        window.location.reload();

    };

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

    const fetchRecipeMcategories = async (selectedLcategory) => {
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

    const handleProductTypeChange = (e) => {
        const value = e.target.value;
        setAdditionalInfo(prevState => ({
            ...prevState,
            productType: value,
            lcategory: '',
            scategory: ''
        }));

        setCategories({
            lcategories: [],
            scategories: []
        });

        setShowLcategories(true);
        setShowScategories(false);
    };

    const handleLcategoryChange = (e) => {
        const value = e.target.value;
        setAdditionalInfo(prevState => ({...prevState, lcategory: value}));

        if (additionalInfo.productType === PRODUCT_TYPES.PROCESSED) {
            fetchRecipeMcategories(value);
        } else if (additionalInfo.productType === PRODUCT_TYPES.RAW) {
            fetchFoodScategories(value);
        }

        setShowScategories(true);
    };

    return (
        <div>
            <h2>Barcode Scanner</h2>
            <video ref={videoRef} autoPlay playsInline/>
            <canvas ref={canvasRef} style={{display: 'none'}}/>
            <button onClick={recognizeText}>바코드 인식</button>
            <Link to="/barlist">
                <button>등록된 상품 보기</button>
            </Link>
            <h3>해당 식품을 직접 입력하실?</h3>
            <Link to="/userinput">
                <button>상품등록</button>
            </Link>
            <div>
                {capturedImage && (
                    <div style={{marginRight: '20px'}}>
                        <h3>찍은 사진 임</h3>
                        <img src={capturedImage} alt="Captured"/>
                    </div>
                )}
                {ocrResult && (
                    <div>
                        <h3>OCR Result:</h3>
                        <textarea
                            value={JSON.stringify(ocrResult, null, 2)}
                            readOnly
                            style={{width: '400px', height: '300px'}}
                        />
                    </div>
                )}

                {concatenatedText && (
                    <div>
                        <h3>인식된 바코드 결과는 ? :</h3>
                        <p>{concatenatedText}</p>
                    </div>
                )}
                {foodSafetyInfo && foodSafetyInfo.C005 && (
                    <div>
                        {foodSafetyInfo.C005.total_count === '0' ? (
                            <div>
                                <h3>해당 바코드에 등록된 상품 정보가 없음</h3>
                                <h3>해당 식품을 직접 입력하실?</h3>
                                <Link to="/userinput">
                                    <button>상품등록</button>
                                </Link>
                            </div>
                        ) : (
                            <div>
                                <h3>추가 정보 입력:</h3>
                                <form>
                                    <label>
                                        제품명:
                                        <input
                                            type="text"
                                            name="productName"
                                            value={additionalInfo.productName}
                                            onChange={handleInputChange}
                                        />
                                    </label><br/>
                                    <p><strong>유통기한:</strong> {additionalInfo.expiryDate}</p><br/>
                                    <p>유통기한은 년월일로 기입해주세요</p>
                                    <label>
                                        유통기한:
                                        <input
                                            type="date"
                                            name="expiryDate"
                                            onChange={handleInputChange}
                                            required
                                        />
                                    </label><br/><br/>
                                    <label>
                                        수량 :
                                        <input
                                            type="text"
                                            name="count"
                                            onChange={handleInputChange}
                                        />
                                    </label>
                                    <label>
                                        <br/><br/>
                                        식품 유형:
                                        <select name="productType" onChange={handleProductTypeChange}
                                                value={additionalInfo.productType}>
                                            <option value="" disabled>========</option>
                                            <option value={PRODUCT_TYPES.PROCESSED}>가공식품</option>
                                            <option value={PRODUCT_TYPES.RAW}>원자재성 식품</option>
                                        </select>
                                    </label>

                                    {showLcategories && (
                                        <div>
                                            <label>종 류:</label>
                                            <select name="lcategory" value={additionalInfo.lcategory}
                                                    onChange={handleLcategoryChange}>
                                                <option value="" disabled>선택하세요</option>
                                                {categories.lcategories.map((category, index) => (
                                                    <option key={index} value={category}>{category}</option>
                                                ))}
                                            </select>
                                        </div>
                                    )}
                                    {showScategories && categories.scategories.length > 0 && (
                                        <div>
                                            <label>세부 종류:</label>
                                            <select name="scategory" value={additionalInfo.scategory}
                                                    onChange={handleInputChange}>
                                                <option value="" disabled>선택하세요</option>
                                                {categories.scategories.map((category, index) => (
                                                    <option key={index} value={category}>{category}</option>
                                                ))}
                                            </select>
                                        </div>
                                    )}
                                    {showScategories && categories.scategories.length === 0 && (
                                        <div>세부 종류가 없습니다.</div>
                                    )}

                                    <button type="button" onClick={saveBarcodeToDB}>저장</button>
                                </form>
                            </div>
                        )}
                    </div>
                )}
            </div>
        </div>
    );
};

export default Barcode;

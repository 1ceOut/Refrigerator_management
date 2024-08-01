import React, { useEffect, useRef, useState } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';

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
    const [lcategories, setLcategories] = useState([]); // lcategory 값을 저장할 상태 변수
    const [scategories, setScategories] = useState([]); // scategory 값을 저장할 상태 변수
    const [showLcategories, setShowLcategories] = useState(false); // 두 번째 select 요소 표시 여부
    const [showScategories, setShowScategories] = useState(false); // 세 번째 select 요소 표시 여부

    useEffect(() => {
        const initCamera = async () => {
            try {
                const stream = await navigator.mediaDevices.getUserMedia({video: true});
                if (stream && videoRef.current) {
                    videoRef.current.srcObject = stream;
                }
            } catch (error) {
                console.error('Error accessing the camera', error);
            }
        };

        initCamera();
    }, []);

    useEffect(() => {
        if (concatenatedText) {
            fetchFoodSafetyInfo(concatenatedText);
        }
    }, [concatenatedText]);

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

        const apiUrl = '/api/custom/v1/32907/69a8f4b90ed9801300e661448e839445d3f21bc6dacddc5bceac7b1e9f1ac92b/general';
        const secretKey = 'R3JMT0JmY05sTGtaWkVvdHVsaHNzSHhWS1VzTVJqd0Y=';

        try {
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
        } catch (error) {
            console.error('Error recognizing text', error);
            if (error.response) {
                console.error('Error response:', error.response.data);
            } else if (error.request) {
                console.error('Error request:', error.request);
            } else {
                console.error('General error:', error.message);
            }
        }
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

        try {
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
        } catch (error) {
            console.error('Error fetching food safety info', error);
        }
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
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

        try {
            const response = await axios.post('http://localhost:8080/api/barcodes', product);
            console.log('Barcode saved successfully:', response.data);
            alert("저장이 되었습니다.");
            window.location.reload();
        } catch (error) {
            console.error('Error saving barcode to DB', error);
        }
    };

    const fetchLcategories = async () => {
        try {
            const username = 'elastic';
            const password = 'finalproject';
            const token = btoa(`${username}:${password}`);

            const response = await axios.post('/food/_search', {
                query: {
                    match_all: {}
                },
                size: 4000
            }, {
                headers: {
                    'Authorization': `Basic ${token}`,
                    'Content-Type': 'application/json'
                }
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
            const username = 'elastic';
            const password = 'finalproject';
            const token = btoa(`${username}:${password}`);

            const response = await axios.post('/food/_search', {
                query: {
                    bool: {
                        must: [
                            { match: { lcategory: selectedLcategory } }
                        ]
                    }
                },
                size: 4000
            }, {
                headers: {
                    'Authorization': `Basic ${token}`,
                    'Content-Type': 'application/json'
                }
            });

            const hits = response.data.hits.hits;
            const uniqueScategories = [...new Set(hits.map(hit => hit._source.scategory))];
            setScategories(uniqueScategories);
        } catch (error) {
            console.error('Error fetching scategories', error);
        }
    };

    const handleProductTypeChangel = (e) => {
        const value = e.target.value;
        setAdditionalInfo((prevInfo) => ({
            ...prevInfo,
            productType: value
        }));

        if (value === '원자재성 식품') {
            fetchLcategories();
            setShowLcategories(true);
        } else {
            setShowLcategories(false);
            setShowScategories(false); // lcategories를 선택하지 않으면 scategories도 숨김
        }
    };

    const handleLcategoryChange = (e) => {
        const value = e.target.value;
        setAdditionalInfo((prevInfo) => ({
            ...prevInfo,
            lcategory: value
        }));

        fetchScategories(value);
        setShowScategories(true);
    };

    return (
        <div className="App">
            {/* 비디오 및 캔버스 요소 */}
            <video ref={videoRef} autoPlay/>
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
                {/* 캡처된 이미지 및 OCR 결과 표시 */}
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

                {/* 바코드 및 식품 안전 정보 표시 */}
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
                                        <select onChange={handleProductTypeChangel} value={additionalInfo.productType}>
                                            <option value="" disabled>========</option>
                                            <option value="가공식품">가공식품</option>
                                            <option value="원자재성 식품">원자재성 식품</option>
                                        </select>
                                    </label>

                                    {showLcategories && (
                                        <label>
                                            종 류:
                                            <select
                                                name="lcategory"
                                                value={additionalInfo.lcategory}
                                                onChange={handleLcategoryChange}
                                            >
                                                {lcategories.map((category, index) => (
                                                    <option key={index} value={category}>{category}</option>
                                                ))}
                                            </select>
                                        </label>
                                    )}

                                    {showScategories && (
                                        <label>
                                            세부 종류:
                                            <select
                                                name="scategory"
                                                value={additionalInfo.scategory}
                                                onChange={handleInputChange}
                                            >
                                                {scategories.map((category, index) => (
                                                    <option key={index} value={category}>{category}</option>
                                                ))}
                                            </select>
                                        </label>
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

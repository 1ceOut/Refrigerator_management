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

    useEffect(() => {
        const initCamera = async () => {
            try {
                const stream = await navigator.mediaDevices.getUserMedia({ video: true });
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
            image.fields.forEach((field) => {
                const filteredText = field.inferText.replace(/\D/g, '');
                if (filteredText) {
                    texts.push(filteredText);
                }
            });
        });

        setInferTexts(texts);

        const concatenatedText = texts.join(' ');
        setConcatenatedText(concatenatedText);
    };

    const fetchFoodSafetyInfo = async (barcode) => {
        const apiKey = '3369ab4c48b84f35a46c';
        const apiUrl = `https://openapi.foodsafetykorea.go.kr/api/${apiKey}/C005/json/1/5/BAR_CD=${barcode}`;

        try {
            const response = await axios.get(apiUrl);
            setFoodSafetyInfo(response.data);
            if (response.data.C005.total_count !== '0') {
                saveBarcodeToDB(response.data.C005.row[0]);
            }
        } catch (error) {
            console.error('Error fetching food safety info', error);
        }
    };

    const saveBarcodeToDB = async (productData) => {
        const product = {
            barcode: productData.BAR_CD,
            productName: productData.PRDLST_NM,
            expiryDate: productData.POG_DAYCNT,
            companyName: productData.BSSH_NM,
            address: productData.SITE_ADDR,
            productType: productData.PRDLST_DCNM,
            permissionDate: productData.PRMS_DT,
        };

        try {
            const response = await axios.post('http://localhost:8080/api/barcodes', product);
            console.log('Barcode saved successfully:', response.data);
        } catch (error) {
            console.error('Error saving barcode to DB', error);
        }
    };

    return (
        <div className="App">
            <video ref={videoRef} autoPlay />
            <canvas ref={canvasRef} style={{ display: 'none' }} />
            <button onClick={recognizeText}>바코드 인식</button>
            <Link to="/saved-barcodes"><button>등록된 상품 보기</button></Link>
            <div>
                <div style={{ display: 'flex', flexDirection: 'row', marginTop: '20px' }}>
                    {capturedImage && (
                        <div style={{ marginRight: '20px' }}>
                            <h3>찍은 사진 임</h3>
                            <img src={capturedImage} alt="Captured" />
                        </div>
                    )}
                    {ocrResult && (
                        <div>
                            <h3>OCR Result:</h3>
                            <textarea
                                value={JSON.stringify(ocrResult, null, 2)}
                                readOnly
                                style={{ width: '400px', height: '300px' }}
                            />
                        </div>
                    )}
                </div>
                <div style={{ marginTop: '20px' }}>
                    {concatenatedText && (
                        <div>
                            <h3>인식된 바코드 결과는 ? :</h3>
                            <p>{concatenatedText}</p>
                        </div>
                    )}
                    {foodSafetyInfo && foodSafetyInfo.C005 && (
                        <div>
                            <h3>총 개수 : {foodSafetyInfo.C005.total_count}</h3>
                            {foodSafetyInfo.C005.total_count === '0' ? (
                                <div>
                                    <h3>해당 바코드에 등록된 상품 정보가 없음</h3>
                                </div>
                            ) : (
                                <div>
                                    <h3>식품 정보:</h3>
                                    {foodSafetyInfo.C005.row.map((item, index) => (
                                        <div key={index} style={{ marginBottom: '20px' }}>
                                            <p><strong>제품명:</strong> {item.PRDLST_NM}</p>
                                            <p><strong>바코드:</strong> {item.BAR_CD}</p>
                                            <p><strong>유통기한:</strong> {item.POG_DAYCNT}</p>
                                            <p><strong>회사명:</strong> {item.BSSH_NM}</p>
                                            <p><strong>주소:</strong> {item.SITE_ADDR}</p>
                                            <p><strong>식품 유형:</strong> {item.PRDLST_DCNM}</p>
                                            <p><strong>허가 날짜:</strong> {item.PRMS_DT}</p>
                                            <hr />
                                        </div>
                                    ))}
                                </div>
                            )}
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
};

export default Barcode;

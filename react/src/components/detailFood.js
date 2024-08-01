import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useParams } from 'react-router-dom';

const DetailFood = () => {
    const { productName, scategory } = useParams();
    const [nutritionInfo, setNutritionInfo] = useState(null);

    const fetchNutritionInfo = async () => {
        try {
                const response = await axios.post('/food/_search',{
                query: {
                    bool: {
                        must: [
                            {
                                wildcard: {
                                    "mcategory": `*${productName}*`
                                }
                            },
                            {
                                wildcard: {
                                    "scategory": `*${scategory}*`
                                }
                            }
                        ]
                    }
                }
            });

            const hits = response.data.hits.hits;
            if (hits.length > 0) {
                setNutritionInfo(hits[0]._source);
            } else {
                setNutritionInfo(null);
            }
        } catch (error) {
            console.error('Error fetching nutrition info', error);
        }
    };

    useEffect(() => {
        fetchNutritionInfo();
    }, []);

    return (
        <div>
            <h2>영양정보</h2>
            <h4>100g 당 영양성분입니다.</h4>
            {nutritionInfo ? (
                <table>
                    <thead>
                    <tr>
                        <th>섬유질</th>
                        <th>칼슘</th>
                        <th>염분</th>
                        <th>칼로리</th>
                        <th>수분</th>
                        <th>탄수화물</th>
                        <th>단백질</th>
                        <th>지방</th>
                        <th>콜레스테롤</th>
                        <th>당분</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>{nutritionInfo.fiber}</td>
                        <td>{nutritionInfo.calcium}</td>
                        <td>{nutritionInfo.salt}</td>
                        <td>{nutritionInfo.kacl}</td>
                        <td>{nutritionInfo.water}</td>
                        <td>{nutritionInfo.carbon}</td>
                        <td>{nutritionInfo.protein}</td>
                        <td>{nutritionInfo.fat}</td>
                        <td>{nutritionInfo.cholesterol}</td>
                        <td>{nutritionInfo.sugar}</td>
                    </tr>


                    </tbody>
                </table>
            ) : (
                <p>해당 영양정보를 찾을 수 없습니다.</p>
            )}
        </div>
    );
};

export default DetailFood;

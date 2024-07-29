// src/camera.js
export default async function getStream(order) {
    if (!navigator.mediaDevices || !navigator.mediaDevices.getUserMedia) {
        const error = new Error('getUserMedia 함수를 사용할 수 없습니다.');
        error.name = 'UserMediaError';
        throw error;
    }

    const devices = await navigator.mediaDevices.enumerateDevices();
    const camera = devices.filter(device => device.kind === 'videoinput');

    const setting = [
        {
            video: {
                deviceId: camera.length ? camera[camera.length - 1].deviceId : null,
                facingMode: { exact: 'environment' },
                focusMode: { ideal: 'continuous' },
                zoom: { ideal: 1 },
            },
        },
        {
            video: {
                deviceId: camera.length ? camera[camera.length - 1].deviceId : null,
                facingMode: { exact: 'environment' },
            },
        },
        {
            video: {
                deviceId: camera.length ? camera[camera.length - 1].deviceId : null,
                focusMode: { ideal: 'continuous' },
                zoom: { ideal: 1 },
            },
        },
        { video: true },
    ];

    try {
        const stream = await navigator.mediaDevices.getUserMedia(setting[order]);
        return stream;
    } catch (e) {
        if (e instanceof Error && e.name === 'NotAllowedError') {
            const error = new Error('카메라 접근에 실패하였습니다.');
            error.name = 'NotAllowedError';
            throw error;
        }
        if (e instanceof Error && e.name === 'NotFoundError') {
            if (order === 3) {
                const error = new Error('접근할 수 있는 카메라가 없습니다.');
                error.name = 'NotFoundError';
                throw error;
            }
        }
    }

    try {
        const stream = await getStream(order + 1);
        return stream;
    } catch (e) {
        if (e instanceof Error) throw e;
    }
    return null;
}

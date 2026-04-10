import apiClient from './config.js';

// Fetch all labs
export const getAllLabs = async () => await apiClient.get('/labs');

// Create new lab
export const createLab = async (labData) => await apiClient.post('/labs', labData);

// Update existing lab
export const updateLab = async (labId, labData) => await apiClient.put(`/labs/${labId}`, labData);

// Delete lab
export const deleteLab = async (labId) => await apiClient.delete(`/labs/${labId}`);

import React, {useState} from "react";
import ApiService from "../service/ApiService";
import ReactMarkdown from "react-markdown";



const GeminiAI = () => {
    const [isOpen, setIsOpen] = useState(false);
    const [inputText, setInputText] = useState('');
    const [response, setResponse] = useState('');

    const togglePopUp = () => {
        setIsOpen(prev => !prev);
        if(!isOpen){
            setInputText('');
            setResponse('');
        }
    }

    const handleSubmit = async (e) => {
        e.preventDefault();
        if(!inputText.trim()) return;
        try {
            const res = await ApiService.getChatAnswer(inputText);
            const messageObj = JSON.parse(res.message);
            const gettingText = messageObj.candidates[0].content.parts[0].text;
            setResponse(gettingText);
            setInputText('');
        } catch (error) {
            console.error(error);
            setResponse('An error occurred');
        }
    };

    return (
        <>
        <button className="ai-button" onClick={togglePopUp}>
            AI
        </button>
        {isOpen && (
            <div className="ai-popup">
                {response && (
                    <div className="ai-response">
                        <ReactMarkdown>{response}</ReactMarkdown>
                        
                    </div>
                )}
                <form onSubmit={handleSubmit}>
                    <input
                    type="text"
                    placeholder="Enter your prompt..."
                    className="ai-input"
                    value={inputText}
                    onChange={(e) => setInputText(e.target.value)}
                    />
                </form>
            </div>
        )}
        </>
    );
}

export default GeminiAI;
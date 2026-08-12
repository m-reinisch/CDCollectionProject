import Modal from 'react-modal';
import {useForm} from "react-hook-form";
import {useEffect} from "react";

type FormValues = {
    barcode: string
}
type MusicBrainzModalProps = {
    isOpen: boolean,
    onClose: () => void,
    onSubmit: (barcode: string) => void,
    onError: (message: string) => void
}

Modal.setAppElement('#root');

const overlayStyle: React.CSSProperties = {
    position: 'fixed', top: 0, left: 0, right: 0, bottom: 0,
    backgroundColor: 'rgba(0, 0, 0, 0.5)', display: 'flex',
    justifyContent: 'center', alignItems: 'center'
};
const modalStyle: React.CSSProperties = {
    backgroundColor: '#fff', padding: '20px', borderRadius: '8px',
    width: '300px', fontSize: 'small'
};
const buttonContainerStyle: React.CSSProperties = {
    display: 'flex', justifyContent: 'space-between',
    marginTop: '15px'
};

export default function MusicBrainzModal(props: Readonly<MusicBrainzModalProps>){
    const { register, handleSubmit, reset, formState: { errors, isValid }
    } = useForm<FormValues>({ mode: 'onChange' })

    function closeModal(){
        reset({barcode: ''})
        props.onClose()
    }
    function submitModal(data: FormValues){
        reset({barcode: ''})
        props.onSubmit(data.barcode)
        props.onClose()
    }

    useEffect(() => {
        if (errors.barcode){
            props.onError(errors.barcode.message!)
        } else {
            props.onError("")
        }
    })
    if (!props.isOpen) return null; // Rendert nichts, wenn es geschlossen ist
    return (
        <Modal overlayClassName="modal-overlay"
               className="modal-content"
               contentLabel={"Mit MusicBrainz suchen"}
               onRequestClose={closeModal}
               isOpen={props.isOpen}>
            <div style={overlayStyle}>
                <form className="modal-form"
                      style={modalStyle}
                      onSubmit={handleSubmit(submitModal)}>
                    <label>
                        Barcode von der CD eingeben:
                        <input type="text"
                               {...register("barcode", {
                                   required: "Der Barcode ist erforderlich!",
                                   pattern: {
                                       value: /\d{12,14}/,
                                       message: "Der Barcode darf nur aus 12 bis 14 zahelen bestehen!"
                                   }
                               })}
                        />
                    </label>
                    <div style={buttonContainerStyle}>
                        <button type="button" onClick={closeModal}>
                            Abbrechen
                        </button>
                        <button type="submit" disabled={!isValid}>
                            Suchen
                        </button>
                    </div>
                </form>
            </div>
        </Modal>
    )
}
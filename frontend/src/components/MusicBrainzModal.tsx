import Modal from 'react-modal';
import {useForm} from "react-hook-form";

type FormValues = {
    barcode: string
}
type MusicBrainzModalProps = {
    isOpen: boolean
}

Modal.setAppElement('#root');

export default function MusicBrainzModal(props: Readonly<MusicBrainzModalProps>){
    const { register, handleSubmit, reset,
        formState: { errors, isValid },
    } = useForm<FormValues>({ mode: 'onChange' });

    function closeModal(){

    }
    function submitModal(){

    }

    if (!props.isOpen) return null; // Rendert nichts, wenn es geschlossen ist
    return (
        <Modal overlayClassName="modal-overlay"
               className="modal-content"
               contentLabel={"Mit MusicBrainz suchen"}
               onRequestClose={closeModal}
               isOpen={props.isOpen}>
            <form className="modal-form"
                  onSubmit={handleSubmit(submitModal)}>
                <label htmlFor="barcode">Barcode</label>
            </form>
        </Modal>
    )
}
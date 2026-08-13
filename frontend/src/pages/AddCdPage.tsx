import "./AddCdPage.css";
import MusicBrainzModal from "../components/MusicBrainzModal.tsx";
import type {CdDTO, FoundCdDTO, Track} from "../types/CdTypes.tsx";
import {type ChangeEvent, useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import {useForm, useFieldArray} from "react-hook-form";
import axios from 'axios';

type FormValues = {
    title: string,
    performer: string,
    publicationYear: string,
    coverUrl: string,
    storageLocation: string,
    trackTT: {title: string, time: string}[]
}
type AddCdPageProps = {
    onChangePage: (page: string) => void,
    onAddCd: (newCd: CdDTO) => void,
    onError: (message: string) => void,
    onPriorError: (message: string) => void
}

const PREDEFINED_VALUES = ['Option 1', 'Option 2', 'Option 3'];

export default function AddCdPage(props: Readonly<AddCdPageProps>) {
    const [isOpen, setIsOpen] = useState(false)
    const [foundCD, setFoundCD] = useState<FoundCdDTO | null>(null)
    const [barcodeError, setBarcodeError] = useState<string>("")
    const [selectedValue, setSelectedValue] = useState<string>("");
    const param= useParams();
    const { control, register, handleSubmit, reset,
            formState: { errors, isValid }
          } = useForm<FormValues>(
              {
                  defaultValues: {
                      title: "",
                      performer: "",
                      publicationYear: "",
                      coverUrl: "",
                      storageLocation: "",
                      trackTT: [{title: "", time: ""},
                                {title: "", time: ""},
                                {title: "", time: ""},
                                {title: "", time: ""},
                                {title: "", time: ""},
                                {title: "", time: ""},
                                {title: "", time: ""},
                                {title: "", time: ""},
                                {title: "", time: ""}]
                  },
                  mode: 'onChange'
              }
    );
    const { fields, append } = useFieldArray({
        control,
        name: "trackTT"
    });

    const handleSelectChange = (e: ChangeEvent<HTMLSelectElement>) => {
        setSelectedValue(e.target.value);
    };

    function submit(data: FormValues) {
        const year: number = Number.parseInt(data.publicationYear, 10)
        const trackList: Track[]= []
        let ix: number = 1;

        data.trackTT.forEach(tt => {
            if (tt.title) {
                const track: Track = {
                    position: ix,
                    trackTitle: tt.title,
                    time: tt.time
                }
                trackList.push(track)
            }
            ix = ix + 1;
        })
        const cd : CdDTO = {
            cdTitle: data.title,
            performer: data.performer,
            publicationYear: year,
            genres: 'LATIN_JAZZ',
            storageLocation: data.storageLocation,
            tracks: trackList,
            coverUrl: data.coverUrl,
            cdCollection: {
                id: param.collId!
            }
        }

        props.onAddCd(cd)
        reset({title: '', performer: '', publicationYear: '',
               coverUrl: ''})
    }
    function searchCd(bc: string) {
        axios.get("/api/musicbrainz/" + bc)
             .then( (response) => {
                 setFoundCD(response.data)
                 props.onPriorError("")
             })
             .catch( (error_) => {
                 props.onPriorError(error_.response.data)
             })
    }
    function initValues() {
        if (foundCD) {
            reset({
                title: foundCD.cdTitle,
                performer: foundCD.performer,
                publicationYear: foundCD.publicationYear.toString(),
                coverUrl: foundCD.coverUrl,
                storageLocation: "",
                trackTT: foundCD.tracks.map( track => (
                    {title: track.trackTitle, time: track.time}
                ))
            })
        }
    }

    useEffect(() => {
        props.onChangePage("add-cd")
    }, [props]);
    useEffect(() => {
        if (errors.title) {
            props.onError(errors.title.message!)
        } else if (errors.performer) {
            props.onError(errors.performer.message!)
        } else if (errors.trackTT) {
            const timeError = fields
                .map((_, index) => errors.trackTT?.[index]?.time)
                .find(Boolean)
            props.onError(timeError ? timeError.message! : "")
        } else if (barcodeError){
            props.onError(barcodeError)
        } else {
            props.onError("")
        }
    }, [barcodeError, errors, fields, props]);
    useEffect(() => {
        initValues()
    }, [foundCD]);

    return (
        <div className="page">
            <div style={{ padding: '7px' }}>
                <button type="button"
                        onClick={ () => {
                            setIsOpen(true);
                            props.onPriorError("")
                        }}>
                    CD in MusicBrainz suchen
                </button>
                <MusicBrainzModal
                    isOpen={isOpen}
                    onClose={() => setIsOpen(false)}
                    onSubmit={searchCd}
                    onError={ (message) =>
                        setBarcodeError(message) }
                />
            </div>
            <form className="new-cd"
                  onSubmit={handleSubmit(submit)}>
                <label id="lbl-cd-titel">
                    CD-Titel:
                    <input id="txt-cd-titel" type="text"
                           {...register("title", {
                               required: "Der Titel der CD ist erforderlich!",
                               pattern: {
                                   value: /\S/,
                                   message: "Der Titel darf nicht nur Leerzeichen haben!"
                               }
                           })}
                    />
                </label>
                <label id="lbl-cd-performer">
                    Interpret:
                    <input id="txt-cd-performer" type="text"
                           {...register("performer", {
                               required: "Der Interpret der CD ist erforderlich!",
                               pattern: {
                                   value: /\S/,
                                   message: "Der Interpret darf nicht nur Leerzeichen haben!"
                               }
                           })}
                    />
                </label>
                <label id="lbl-cd-year">
                    Jahr der Veröffentlichung:
                    <input id="txt-cd-year" type="text"
                           {...register("publicationYear")}
                    />
                </label>
                <label id="lbl-cd-cover">
                    URL für Cover Bild:
                    <input id="txt-cd-cover" type="text"
                           {...register("coverUrl")}
                    />
                </label>
                <label id="lbl-cd-style">
                    Stil-Richtung:
                    <select id="sel-cd-style" value={selectedValue}
                            onChange={handleSelectChange}>
                        <option value="">nichts ausgewählt</option>
                        {PREDEFINED_VALUES.map((val) => (
                            <option key={val} value={val}>
                                {val}
                            </option>
                        ))}
                    </select>
                </label>
                <label id="lbl-cd-storage">
                    Ablageort:
                    <input id="txt-cd-storage" type="text"
                           {...register("storageLocation")}
                    />
                </label>
                <label className="tracks">
                    Stücke:
                    {fields.map( (field, index) => (
                    <label className="track" key={field.id}>
                        {index + 1}.
                        <label className="lbl-track-titel">
                            Titel:
                            <input className="txt-track-titel"
                                   type="text"
                                   {...register(`trackTT.${index}.title`)}
                            />
                        </label>
                        <label className="lbl-track-time">
                            Zeit:
                            <input className="txt-track-time"
                                   type="text"
                                   {...register(`trackTT.${index}.time`, {
                                       pattern: {
                                           value: /[0-5]?\d:[0-5]\d/,
                                           message: "Die Zeit muss das Format mm:ss oder m:ss haben!"
                                       }
                                   })}
                            />
                        </label>
                    </label>
                    ))}
                </label>
                <div className="new-cd-footer">
                    <button id="more-tracks-btn" type="button"
                            onClick={ () =>
                                append({title: "", time: ""})}>
                        Mehr Stücke
                    </button>
                    <button id="add-cd-btn" type="submit"
                            disabled={!isValid}
                            onClick={handleSubmit(submit)}>
                        CD abspeichern
                    </button>
                </div>
            </form>
        </div>
    )
}
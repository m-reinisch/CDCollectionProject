import "./AddCdPage.css";
import MusicBrainzModal from "../components/MusicBrainzModal.tsx";
import type {CdDTO, FoundCdDTO, Track} from "../types/CdTypes.tsx";
import {useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import {useForm, useFieldArray} from "react-hook-form";
import axios from 'axios';

type FormValues = {
    title: string,
    performer: string,
    publicationYear: string,
    coverUrl: string | null,
    trackTT: {title: string, time: string}[]
}
type AddCdPageProps = {
    onChangePage: (page: string) => void,
    onAddCd: (newCd: CdDTO) => void,
    onError: (message: string) => void,
    onPriorError: (message: string) => void
}

export default function AddCdPage(props: Readonly<AddCdPageProps>) {
    const [isOpen, setIsOpen] = useState(false)
    const [foundCD, setFoundCD] = useState<FoundCdDTO | null>(null)
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
                      trackTT: [{title: "", time: ""},
                                {title: "", time: ""},
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
            tracks: trackList,
            coverUrl: null,
            cdCollection: {
                id: param.collId!
            }
        }

        props.onAddCd(cd)
        reset({title: '', performer: '', publicationYear: ''})
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
                trackTT: foundCD.tracks.map( track => (
                    {title: track.trackTitle, time: track.time}
                ))
            })
        }
    }

    useEffect(() => {
        props.onChangePage("add-cd")
    }, []);
    useEffect(() => {
        if (errors.title) {
            props.onError(errors.title.message!)
        } else if (errors.performer) {
            props.onError(errors.performer.message!)
        } else {
            props.onError("")
        }
    }, [errors, props]);
    useEffect(() => {
        initValues()
    }, [foundCD]);

    return (
        <div className="page">
            <div style={{ padding: '7px' }}>
                <button type="button"
                        onClick={() => setIsOpen(true)}>
                    CD in MusicBrainz suchen
                </button>
                <MusicBrainzModal
                    isOpen={isOpen}
                    onClose={() => setIsOpen(false)}
                    onSubmit={searchCd}
                />
            </div>
            <form className="new-cd"
                  onSubmit={handleSubmit(submit)}>
                <label id="lbl-cd-titel">
                    CD-Titel:
                    <input id="txt-cd-titel" type="text"
                           {...register("title", {
                               required: "Der Titel der CD ist erforderlich!"
                           })}
                    />
                </label>
                <label id="lbl-cd-perform">
                    Interpret:
                    <input id="txt-cd-perform" type="text"
                           {...register("performer", {
                               required: "Der Interpret der CD ist erforderlich!"
                           })}
                    />
                </label>
                <label id="lbl-cd-year">
                    Jahr der Veröffentlichung:
                    <input id="txt-cd-year" type="text"
                           {...register("publicationYear")}
                    />
                </label>
                <label id="lbl-cd-style"></label>
                <label id="lbl-cd-storage"></label>
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
                                   {...register(`trackTT.${index}.time`)}
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